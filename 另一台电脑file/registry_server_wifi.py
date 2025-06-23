# ==============================================================
#  registry_server_elect.py
#  dual-port Registry  (50051 biz  |  50052 heartbeat)
#  Bully-style leader election by longest-uptime
# ==============================================================

import grpc, time, json, sqlite3, threading, schedule, uuid
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime

import object_repository_pb2       as pb
import object_repository_pb2_grpc   as pb_grpc
import heartbeat_service_pb2        as hb_pb
import heartbeat_service_pb2_grpc   as hb_grpc
from google.protobuf import empty_pb2

# ---------- run-time constants --------------------------------
NODE_ID     = str(uuid.uuid4())[:8]
START_TS    = time.time()
ELECT_INT   = 2.0         # seconds
RPC_TIMEOUT = 1.0

# ==============================================================


class ObjectRegistry(pb_grpc.ObjectRepositoryServicer):
    """Business service on :50051"""

    def __init__(self, cfg: dict, hb_servicer):
        # basic meta ----------------------------------------------------------
        self.cfg        = cfg
        self.peers_cfg  = cfg["peers"]
        self.role       = "primary" if cfg.get("bootstrap_primary", False) else "backup"
        self.leader_id  = NODE_ID if self.role == "primary" else None
        self.is_leader  = (self.role == "primary")
        hb_servicer.bind_registry(self)      # give heartbeat layer a pointer

        # local store ---------------------------------------------------------
        self.objects = {}
        self.lock    = threading.Lock()
        self.conn    = sqlite3.connect(cfg["database"], check_same_thread=False)
        self._init_db(); self._load_state()

        # stubs to peers ------------------------------------------------------
        self.peer_biz = {
            p["id"]: pb_grpc.ObjectRepositoryStub(
                grpc.insecure_channel(f"{p['host']}:{p['biz_port']}"))
            for p in self.peers_cfg
        }
        self.peer_hb = {
            p["id"]: hb_grpc.HeartbeatServiceStub(
                grpc.insecure_channel(f"{p['host']}:{p['hb_port']}"))
            for p in self.peers_cfg
        }

        self.ttl_seconds = cfg.get("ttl_seconds", 300)

        # background threads --------------------------------------------------
        schedule.every(5).minutes.do(self.save_state)
        threading.Thread(target=self._schedule_loop, daemon=True).start()
        threading.Thread(target=self._ttl_gc,       daemon=True).start()
        threading.Thread(target=self._elect_loop,   daemon=True).start()

    # =============== DB helpers =============================================
    def _init_db(self):
        cur=self.conn.cursor()
        cur.execute("""CREATE TABLE IF NOT EXISTS objects(
                       name TEXT PRIMARY KEY, address TEXT, language TEXT,
                       version TEXT, region TEXT, last_seen REAL)""")
        self.conn.commit()

    def _load_state(self):
        cur=self.conn.cursor(); cur.execute("SELECT * FROM objects")
        for n,a,l,v,r,ts in cur.fetchall():
            self.objects[n]=dict(address=a,language=l,version=v,
                                 region=r,last_seen=ts,source="local")
        print(f"✅ Loaded {len(self.objects)} snapshot rows")

    def save_state(self):
        with self.lock:
            cur=self.conn.cursor()
            for n,inf in self.objects.items():
                cur.execute("REPLACE INTO objects VALUES (?,?,?,?,?,?)",
                            (n,inf["address"],inf["language"],
                             inf["version"],inf["region"],inf["last_seen"]))
            self.conn.commit()
            print("✅ Periodic state saved")

    # =============== background loops =======================================
    def _schedule_loop(self):
        while True: schedule.run_pending(); time.sleep(1)

    def _ttl_gc(self):
        while True:
            now=time.time(); exp=[]
            with self.lock:
                for n,inf in self.objects.items():
                    if inf["source"]=="local" and now-inf["last_seen"]>self.ttl_seconds:
                        exp.append(n)
                for n in exp:
                    print(f"⚠ TTL expired -> {n}")
                    self._delete_db(n); del self.objects[n]
            time.sleep(5)

    # ---------------- leader election loop ----------------------------------
    def _elect_loop(self):
        while True:
            try:
                stats=[(NODE_ID, time.time()-START_TS)]
                for p in self.peers_cfg:
                    try:
                        r=self.peer_hb[p["id"]].GetUptime(
                            empty_pb2.Empty(), timeout=RPC_TIMEOUT)
                        stats.append((r.node_id, r.uptime_sec))
                    except Exception:
                        pass
                stats.sort(key=lambda x: -x[1])
                new_leader=stats[0][0]

                if new_leader!=self.leader_id:        # role change
                    self.leader_id=new_leader
                    self.is_leader=(NODE_ID==new_leader)
                    role="PRIMARY" if self.is_leader else "BACKUP"
                    print(f"[ROLE] now {role}  (leader={self.leader_id})")
            except Exception as e:
                print("[ELECT] error:", e)
            time.sleep(ELECT_INT)

    # =============== CRUD RPC  (only leader accepts) ========================
    def _reject_if_backup(self, ctx):
        ctx.set_code(grpc.StatusCode.FAILED_PRECONDITION)
        ctx.set_details("not leader")

    def RegisterObject(self, req, ctx):
        if not self.is_leader and not req.is_replication:
            return self._reject_if_backup(ctx) or pb.RegisterResponse(success=False)

        info=dict(address=req.object_address,language=req.language,
                  version=req.version,region=req.region,
                  last_seen=time.time(),source="local")
        with self.lock:
            self.objects[req.object_name]=info; self._save_row(req.object_name,info)
        print(f"[REGISTER] {req.object_name}")

        # replicate to backups
        if self.is_leader and not req.is_replication:
            for sid,stub in self.peer_biz.items():
                try:
                    stub.RegisterObject(pb.RegisterRequest(
                        object_name=req.object_name,
                        object_address=req.object_address,
                        language=req.language,version=req.version,
                        region=req.region,is_replication=True), timeout=RPC_TIMEOUT)
                except Exception: pass
        return pb.RegisterResponse(success=True)

    def DeregisterObject(self, req, ctx):
        if not self.is_leader and not req.is_replication:
            return self._reject_if_backup(ctx) or pb.DeregisterResponse(success=False)

        with self.lock:
            if req.object_name in self.objects:
                self._delete_db(req.object_name); del self.objects[req.object_name]
        print(f"[DEREGISTER] {req.object_name}")

        if self.is_leader and not req.is_replication:
            for sid,stub in self.peer_biz.items():
                try:
                    stub.DeregisterObject(pb.DeregisterRequest(
                        object_name=req.object_name,is_replication=True),
                        timeout=RPC_TIMEOUT)
                except Exception: pass
        return pb.DeregisterResponse(success=True)

    def GetObject(self, req, ctx):
        with self.lock:
            inf=self.objects.get(req.object_name)
            return pb.GetResponse(object_address=inf["address"] if inf else "")

    def ListObjects(self, req, ctx):
        with self.lock:
            return pb.ObjectListResponse(objects=[
                pb.ObjectInfo(object_name=n,
                              object_address=inf["address"],
                              language=inf["language"],
                              version =inf["version"],
                              region  =inf["region"],
                              last_seen=inf["last_seen"])
                for n,inf in self.objects.items()])

    def Heartbeat(self, req, ctx):
        with self.lock:
            if req.object_name in self.objects:
                self.objects[req.object_name]["last_seen"]=time.time()
                self._save_row(req.object_name,self.objects[req.object_name])
                print("[HEARTBEAT]", req.object_name, "ok")
                return pb.HeartbeatAck(ok=True)
        return pb.HeartbeatAck(ok=False)

    # db helpers -------------------------------------------------------------
    def _save_row(self, n, inf):
        cur=self.conn.cursor()
        cur.execute("REPLACE INTO objects VALUES (?,?,?,?,?,?)",
                    (n,inf["address"],inf["language"],inf["version"],
                     inf["region"],inf["last_seen"]))
        self.conn.commit()

    def _delete_db(self, n):
        cur=self.conn.cursor(); cur.execute("DELETE FROM objects WHERE name=?", (n,))
        self.conn.commit()


# ==========================================================================


class HeartbeatServicer(hb_grpc.HeartbeatServiceServicer):
    """
    Separate lightweight service on :50052
    - Ping     : used by clients (only leader replies OK)
    - GetUptime: used among servers for leader election
    """

    def __init__(self):
        self._registry: ObjectRegistry|None = None

    def bind_registry(self, reg: ObjectRegistry):
        self._registry = reg

    # -------- client heartbeat ---------------------------------------------
    def Ping(self, req, ctx):
        if self._registry and self._registry.is_leader:
            # positive ack from PRIMARY
            print("[PING]", datetime.now().strftime("%H:%M:%S"),
                  "from", ctx.peer())
            return empty_pb2.Empty()
        # backup returns UNAVAILABLE so client will try next node
        ctx.abort(grpc.StatusCode.UNAVAILABLE, "not primary")

    # -------- server-to-server election ------------------------------------
    def GetUptime(self, req, ctx):
        print("[GETUPTIME] From:", ctx.peer())
        return hb_pb.UptimeInfo(node_id=NODE_ID,
                                uptime_sec=time.time()-START_TS)


# ============================ main =========================================
def serve():
    with open("config.json", encoding="utf-8") as f:
        cfg = json.load(f)

    hb_servicer = HeartbeatServicer()
    reg = ObjectRegistry(cfg, hb_servicer)

    # 服务注册
    biz_srv = grpc.server(ThreadPoolExecutor(max_workers=10))
    pb_grpc.add_ObjectRepositoryServicer_to_server(reg, biz_srv)
    biz_srv.add_insecure_port(cfg["self_address"])

    hb_srv = grpc.server(ThreadPoolExecutor(max_workers=4))
    hb_grpc.add_HeartbeatServiceServicer_to_server(hb_servicer, hb_srv)
    hb_srv.add_insecure_port(cfg["hb_address"])

    # 启动服务
    biz_srv.start()
    hb_srv.start()
    print("✅ biz @", cfg["self_address"])
    print("✅ hb  @", cfg["hb_address"])
    print("✅ Heartbeat gRPC service registered and running on",cfg["hb_address"])

    biz_srv.wait_for_termination()
    hb_srv.wait_for_termination()

# ---------------------------------------------------------------------------
if __name__ == "__main__":
    serve()
