# test_client.py
import grpc
import threading
import time
import object_repository_pb2 as pb
import object_repository_pb2_grpc as pbg

# 服务器IP（192.168.5.44）
server_ip = '192.168.5.44'
channel = grpc.insecure_channel(f'{server_ip}:50051')
stub = pbg.ObjectRepositoryStub(channel)

# 线程停止标志
stop_event = threading.Event()

def heartbeat(name):
    while not stop_event.is_set():
        print(f"[HEARTBEAT] Sending heartbeat at {time.ctime()} for {name}")
        try:
            ack = stub.Heartbeat(pb.HeartbeatPing(object_name=name))
            if not ack.ok:
                print("⚠️  Heartbeat rejected, need re-register")
        except grpc.RpcError as e:
            print(f"[HEARTBEAT] Error: {e}")
        time.sleep(3)

def main():
    name = "python_Calculator"
    max_retries = 3
    retry_delay = 5
    # 客户端自己的IP（192.168.5.42）
    wifi_ip = '192.168.5.42'
    wifi_port = 6000

    for attempt in range(max_retries):
        try:
            # 注册
            stub.RegisterObject(pb.RegisterRequest(
                object_name=name, object_address=f"{wifi_ip}:{wifi_port}",
                language="Python", version="1.0", region="EU"))
            print("REGISTER done")
            break
        except grpc.RpcError as e:
            if attempt < max_retries - 1:
                print(f"Registration attempt {attempt + 1} failed. Retrying in {retry_delay} seconds... Error: {e}")
                time.sleep(retry_delay)
            else:
                print(f"Failed to register after {max_retries} attempts. Error: {e}")
                return

    # 启动心跳线程
    heartbeat_thread = threading.Thread(target=heartbeat, args=(name,))
    heartbeat_thread.daemon = True
    heartbeat_thread.start()

    # 查看
    resp = stub.GetObject(pb.GetRequest(object_name=name))
    print("LOOKUP ->", resp.object_address)

    # 休眠 20 s 观察 TTL
    time.sleep(15)

    # 注销
    print(f"[DEREGISTER] Sending deregister request at {time.ctime()} for {name}")
    stub.DeregisterObject(pb.DeregisterRequest(object_name=name))
    print("DEREGISTER done")

    # 设置停止标志并等待线程结束
    stop_event.set()
    # 给线程一点时间来清理
    time.sleep(0.5)

if __name__ == "__main__":
    main()