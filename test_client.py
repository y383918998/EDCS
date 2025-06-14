import grpc, threading, time
import object_repository_pb2 as pb
import object_repository_pb2_grpc as pbg

channel = grpc.insecure_channel('localhost:50051')
stub    = pbg.ObjectRepositoryStub(channel)

def heartbeat(name):
    while True:
        print(f"[HEARTBEAT] Sending heartbeat at {time.ctime()} for {name}")
        ack = stub.Heartbeat(pb.HeartbeatPing(object_name=name))
        if not ack.ok:
            print("⚠️  Heartbeat rejected, need re-register")
        time.sleep(3)

def main():
    name = "Calculator"

    # 注册
    stub.RegisterObject(pb.RegisterRequest(
        object_name=name, object_address="127.0.0.1:6000",
        language="Python", version="1.0", region="EU"))
    print("REGISTER done")

    # 心跳线程
    threading.Thread(target=heartbeat, args=(name,), daemon=True).start()

    # 查看
    resp = stub.GetObject(pb.GetRequest(object_name=name))
    print("LOOKUP ->", resp.object_address)

    # 休眠 20 s 观察 TTL（把 TTL_SECONDS 调到 15，可以看到服务器剔除日志）
    time.sleep(15)

    # 注销
    print(f"[DEREGISTER] Sending deregister request at {time.ctime()} for {name}")
    stub.DeregisterObject(pb.DeregisterRequest(object_name=name))
    print("DEREGISTER done")

if __name__ == "__main__":
    main()