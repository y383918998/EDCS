#include <iostream>
#include <fcntl.h>
#include <io.h>
#include <string>

#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN
#endif
#ifndef NOMINMAX
#define NOMINMAX
#endif
#ifndef NOGDI
#define NOGDI
#endif

#include <grpcpp/grpcpp.h>
#include "object_repository.grpc.pb.h"

namespace obj = objectrepo;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;

class RegistryClient {
public:
    RegistryClient(std::shared_ptr<Channel> ch, const std::string& name, const std::string& addr)
        : stub_(obj::ObjectRepository::NewStub(ch)), object_name_(name), object_address_(addr) {}

    bool Register() {
        obj::RegisterRequest req;
        req.set_object_name(object_name_);
        req.set_object_address(object_address_);
        req.set_language("C++");
        req.set_version("1.0");
        req.set_region("PL");  // ✅ 波兰

        obj::RegisterResponse resp;
        ClientContext ctx;
        Status st = stub_->RegisterObject(&ctx, req, &resp);
        return st.ok() && resp.success();
    }

    std::string Lookup() {
        obj::GetRequest req;
        req.set_object_name(object_name_);

        obj::GetResponse resp;
        ClientContext ctx;
        Status st = stub_->GetObject(&ctx, req, &resp);
        return st.ok() ? resp.object_address() : "";
    }

    bool Heartbeat() {
        obj::HeartbeatPing ping;
        ping.set_object_name(object_name_);

        obj::HeartbeatAck ack;
        ClientContext ctx;
        Status st = stub_->Heartbeat(&ctx, ping, &ack);
        return st.ok() && ack.ok();
    }

    bool Deregister() {
        obj::DeregisterRequest req;
        req.set_object_name(object_name_);

        obj::DeregisterResponse resp;
        ClientContext ctx;
        Status st = stub_->DeregisterObject(&ctx, req, &resp);
        return st.ok() && resp.success();
    }

private:
    std::unique_ptr<obj::ObjectRepository::Stub> stub_;
    std::string object_name_;
    std::string object_address_;
};

int main() {
    _setmode(_fileno(stdout), _O_U16TEXT);

    std::string server_ip = "192.168.5.44:50051"; 
    std::string object_name = "Cpp_Calculator";   // ✅ 自定义客户端名称
    std::string my_object_address = "192.168.5.42:8000";

    auto channel = grpc::CreateChannel(server_ip, grpc::InsecureChannelCredentials());
    RegistryClient cli(channel, object_name, my_object_address);

    if (cli.Register())
        std::wcout << L"✔ Registered\n";

    std::wcout << L"Lookup -> " << cli.Lookup().c_str() << L'\n';
    std::wcout << L"Heartbeat ok? " << std::boolalpha << cli.Heartbeat() << L'\n';

    cli.Deregister();
    std::wcout << L"✔ Deregistered\n";
    return 0;
}
