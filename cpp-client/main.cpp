#include <iostream>
#include <fcntl.h>
#include <io.h>

// ==== 防止 windows.h 把 GetObject 宏化成 GetObjectA/W ====
#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN   // 精简 windows 头
#endif
#ifndef NOMINMAX
#define NOMINMAX              // 禁掉 min/max 宏
#endif
#ifndef NOGDI
#define NOGDI                 // 完全屏蔽 GDI → 不再生成 GetObject 宏
#endif
// ===========================================================

#include <grpcpp/grpcpp.h>
#include "object_repository.grpc.pb.h"

namespace obj = objectrepo;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;

class RegistryClient {
public:
    explicit RegistryClient(std::shared_ptr<Channel> ch)
        : stub_(obj::ObjectRepository::NewStub(ch)) {}

    bool Register() {
        obj::RegisterRequest req;
        req.set_object_name("Calculator");
        req.set_object_address("127.0.0.1:8000");
        req.set_language("C++");
        req.set_version("1.0");
        req.set_region("US");

        obj::RegisterResponse resp;
        ClientContext ctx;
        Status st = stub_->RegisterObject(&ctx, req, &resp);
        return st.ok() && resp.success();
    }

    std::string Lookup() {
        obj::GetRequest req;
        req.set_object_name("Calculator");

        obj::GetResponse resp;
        ClientContext ctx;
        Status st = stub_->GetObject(&ctx, req, &resp);
        return st.ok() ? resp.object_address() : "";
    }

    bool Heartbeat() {
        obj::HeartbeatPing ping;
        ping.set_object_name("Calculator");

        obj::HeartbeatAck ack;
        ClientContext ctx;
        Status st = stub_->Heartbeat(&ctx, ping, &ack);
        return st.ok() && ack.ok();
    }

    bool Deregister() {
        obj::DeregisterRequest req;
        req.set_object_name("Calculator");

        obj::DeregisterResponse resp;
        ClientContext ctx;
        Status st = stub_->DeregisterObject(&ctx, req, &resp);
        return st.ok() && resp.success();
    }

private:
    std::unique_ptr<obj::ObjectRepository::Stub> stub_;
};

int main() {
    // 设置控制台输出为 UTF-8 编码
    _setmode(_fileno(stdout), _O_U16TEXT);
    auto channel = grpc::CreateChannel("localhost:50051",
                                       grpc::InsecureChannelCredentials());
    RegistryClient cli(channel);

    if (cli.Register())
        std::wcout << L"✔ Registered\n";

    std::wcout << L"Lookup -> " << cli.Lookup().c_str() << L'\n';
    std::wcout << L"Heartbeat ok? " << std::boolalpha << cli.Heartbeat() << L'\n';

    cli.Deregister();
    std::wcout << L"✔ Deregistered\n";
    return 0;
}