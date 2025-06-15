# registry_server.py  -- 线程池 + SQLite + 心跳/TTL
import grpc, sqlite3, threading, time, os
from concurrent import futures
from pathlib import Path
import object_repository_pb2 as pb
import object_repository_pb2_grpc as pbg

DB_PATH        = "registry.db"
TTL_SECONDS    = 15          # 心跳超时时间
GC_INTERVAL    = 5           # 清理周期

class ObjectRepositoryServicer(pbg.ObjectRepositoryServicer):
    def __init__(self, db_path: str):
        self.db_path = db_path
        self.lock    = threading.RLock()
        self.objects = {}     # object_name -> dict(meta + last_seen)
        self._init_db()
        threading.Thread(target=self._gc_loop, daemon=True).start()

    # ---------- gRPC ----------
    def RegisterObject(self, request, context):
        with self.lock:
            info = {"address": request.object_address, "language": request.language,
                    "version": request.version, "region": request.region,
                    "last_seen": time.time()}
            self.objects[request.object_name] = info
            self._save_row(request.object_name, info)
        print(f"[REGISTER] {request.object_name} -> {request.object_address}")
        return pb.RegisterResponse(success=True)

    def DeregisterObject(self, request, context):
        with self.lock:
            ok = self.objects.pop(request.object_name, None) is not None
            if ok:
                self._delete_row(request.object_name)
                print(f"[DEREGISTER] {request.object_name}")
        return pb.DeregisterResponse(success=ok)

    def UpdateObject(self, request, context):
        with self.lock:
            if request.object_name not in self.objects:
                print(f"[UPDATE] {request.object_name} not found")
                return pb.UpdateResponse(success=False)

            info = {"address": request.object_address, "language": request.language,
                    "version": request.version, "region": request.region,
                    "last_seen": time.time()}
            self.objects[request.object_name] = info
            self._save_row(request.object_name, info)
            print(f"[UPDATE] {request.object_name} updated")
        return pb.UpdateResponse(success=True)

    def GetObject(self, request, context):
        obj = self.objects.get(request.object_name)
        addr = obj["address"] if obj else ""
        print(f"[LOOKUP] {request.object_name} -> {addr or 'NOT FOUND'}")
        return pb.GetResponse(object_address=addr)

    def ListObjects(self, request, context):
        resp = pb.ObjectListResponse()
        with self.lock:
            for name, info in self.objects.items():
                resp.objects.add(
                    object_name=name, object_address=info["address"],
                    language=info["language"], version=info["version"],
                    region=info["region"])
        print(f"[LIST] {len(resp.objects)} objects")
        return resp

    # ---------- 新增：Heartbeat ----------
    def Heartbeat(self, request, context):
        with self.lock:
            if request.object_name in self.objects:
                print(f"[HEARTBEAT] Received heartbeat at {time.ctime()} for {request.object_name}")
                self.objects[request.object_name]["last_seen"] = time.time()
                return pb.HeartbeatAck(ok=True)
            print(f"[HEARTBEAT] Rejected heartbeat at {time.ctime()} for {request.object_name} (object not found)")
        return pb.HeartbeatAck(ok=False)

    # ---------- SQLite -----
    def _init_db(self):
        first = not Path(self.db_path).exists()
        conn = sqlite3.connect(self.db_path)
        conn.execute("""CREATE TABLE IF NOT EXISTS objects(
                        name TEXT PRIMARY KEY,
                        address TEXT, language TEXT, version TEXT,
                        region TEXT, last_seen REAL)""")
        conn.commit()

        # 加载已有数据
        cur = conn.execute("SELECT name,address,language,version,region,last_seen FROM objects")
        rows = cur.fetchall()
        with self.lock:
            for n,a,l,v,r,ts in rows:
                self.objects[n] = {"address":a,"language":l,"version":v,"region":r,"last_seen":ts}
        conn.close()
        if not first:
            print(f"▶ Loaded {len(self.objects)} records from snapshot")

    def _save_row(self, name, info):
        conn = sqlite3.connect(self.db_path)
        conn.execute("""REPLACE INTO objects
                       (name,address,language,version,region,last_seen)
                       VALUES(?,?,?,?,?,?)""",
                     (name, info["address"], info["language"],
                      info["version"], info["region"], info["last_seen"]))
        conn.commit()
        conn.close()

    def _delete_row(self, name):
        conn = sqlite3.connect(self.db_path)
        conn.execute("DELETE FROM objects WHERE name=?", (name,))
        conn.commit()
        conn.close()

    # ---------- TTL 清理 ----------
    def _gc_loop(self):
        while True:
            time.sleep(GC_INTERVAL)
            now = time.time()
            expired = []
            with self.lock:
                for name, info in list(self.objects.items()):
                    if now - info["last_seen"] > TTL_SECONDS:
                        expired.append(name)
                        del self.objects[name]
            for n in expired:
                self._delete_row(n)
                print(f"⚠️  TTL expired -> {n} removed")

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pbg.add_ObjectRepositoryServicer_to_server(ObjectRepositoryServicer(DB_PATH), server)
    
    # 明确指定监听的 IP 地址（可选）
    server.add_insecure_port('192.168.5.44:50051')
    # 或者继续监听所有接口（推荐）
    # server.add_insecure_port('[::]:50051')
    
    server.start()
    print("✅ Registry server is running on port 50051...")
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        print("\n⏹️  CTRL-C, server stopped.")

if __name__ == '__main__':
    serve()