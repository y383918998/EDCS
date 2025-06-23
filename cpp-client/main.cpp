/******************************************************************
 * main.cpp – Dual-port client (Ping + business Heartbeat)
 ******************************************************************/
#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <atomic>
#include <vector>
#include <winsock2.h>
#include <iphlpapi.h>
#include <conio.h>
#include <grpcpp/grpcpp.h>
#include "object_repository.grpc.pb.h"
#include "heartbeat_service.grpc.pb.h"

#pragma comment(lib, "IPHLPAPI.lib")
#pragma comment(lib, "ws2_32.lib")

std::string GetLocalIPAddress() {
    char buf[1024]; DWORD sz = sizeof(buf);
    if (GetAdaptersInfo(reinterpret_cast<PIP_ADAPTER_INFO>(buf), &sz)
        == ERROR_BUFFER_OVERFLOW) return "";
    auto* adp = reinterpret_cast<PIP_ADAPTER_INFO>(buf);
    while (adp) {
        if (adp->Type == MIB_IF_TYPE_ETHERNET || adp->Type == IF_TYPE_IEEE80211)
            return adp->IpAddressList.IpAddress.String;
        adp = adp->Next;
    }
    return "";
}

class RegistryClient {
public:
    RegistryClient(const std::vector<std::string>& biz_addrs,
                   const std::vector<std::string>& hb_addrs)
    {
        for (auto& a : biz_addrs)
            biz_stubs_.emplace_back(objectrepo::ObjectRepository::NewStub(
                grpc::CreateChannel(a, grpc::InsecureChannelCredentials())));

        for (auto& a : hb_addrs)
            hb_stubs_.emplace_back(hb::HeartbeatService::NewStub(
                grpc::CreateChannel(a, grpc::InsecureChannelCredentials())));
    }

    bool Register(const std::string& name,const std::string& addr,
                  const std::string& lang,const std::string& ver,
                  const std::string& region)
    {
        objectrepo::RegisterRequest req;
        req.set_object_name(name); req.set_object_address(addr);
        req.set_language(lang);    req.set_version(ver);
        req.set_region(region);    req.set_is_replication(false);
        objectrepo::RegisterResponse resp;

        for (size_t i = 0; i < biz_stubs_.size(); ++i) {
            auto& st = biz_stubs_[i];
            grpc::ClientContext ctx;
            ctx.set_deadline(std::chrono::system_clock::now() + std::chrono::milliseconds(1500));
            if (st->RegisterObject(&ctx, req, &resp).ok() && resp.success()) {
                biz_idx_ = i; // ✅ 切换业务主节点索引
                return true;
            }
        }
        return false;
    }

    bool Deregister(const std::string& name)
    {
        objectrepo::DeregisterRequest req;
        req.set_object_name(name); req.set_is_replication(false);
        objectrepo::DeregisterResponse resp;
        return TryAllBiz([&](auto& s, grpc::ClientContext& ctx){
            return s->DeregisterObject(&ctx, req, &resp).ok() && resp.success();
        });
    }

    std::string Lookup(const std::string& name)
    {
        objectrepo::GetRequest req; req.set_object_name(name);
        objectrepo::GetResponse resp;
        bool ok = TryAllBiz([&](auto& s, grpc::ClientContext& ctx){
            return s->GetObject(&ctx, req, &resp).ok();
        });
        return ok ? resp.object_address() : "";
    }

    bool Ping() {
        google::protobuf::Empty em;
        return TryAllHb([&](auto& s, grpc::ClientContext& ctx){
            return s->Ping(&ctx, em, &em).ok();
        });
    }

    bool Heartbeat(const std::string& name) {
        objectrepo::HeartbeatPing ping;
        ping.set_object_name(name);
        objectrepo::HeartbeatAck ack;
        return TryAllBiz([&](auto& s, grpc::ClientContext& ctx){
            return s->Heartbeat(&ctx, ping, &ack).ok() && ack.ok();
        });
    }

    void ensureAlive(const std::string& n,const std::string& a,
                     const std::string& l,const std::string& v,
                     const std::string& r)
    {
        if (!Ping()) {
            std::cout << "[Warn] Ping failed, switching...\n";
            if (Register(n,a,l,v,r))
                std::cout << "  -> Re-registered on new leader\n";
            else
                std::cout << "  -> Re-register failed\n";
        } else {
            if (!Heartbeat(n))
                std::cout << "[Warn] Heartbeat failed (unexpected)\n";
        }
    }

private:
    template<typename F>
    bool TryAllBiz(F&& f){
        size_t n = biz_stubs_.size();
        for (size_t k = 0; k < n; ++k){
            size_t i = (biz_idx_ + k) % n;
            auto& st = biz_stubs_[i];
            grpc::ClientContext ctx;
            ctx.set_deadline(std::chrono::system_clock::now()
                             + std::chrono::milliseconds(1500));
            if (f(st, ctx)) {
                biz_idx_ = i;
                return true;
            }
        }
        return false;
    }

    template<typename F>
    bool TryAllHb(F&& f){
        size_t n = hb_stubs_.size();
        for (size_t k = 0; k < n; ++k){
            size_t i = (hb_idx_ + k) % n;
            auto& st = hb_stubs_[i];
            grpc::ClientContext ctx;
            ctx.set_deadline(std::chrono::system_clock::now()
                             + std::chrono::milliseconds(1000));
            if (f(st, ctx)) {
                hb_idx_ = i;
                return true;
            }
        }
        return false;
    }

    std::vector<std::unique_ptr<objectrepo::ObjectRepository::Stub>> biz_stubs_;
    std::vector<std::unique_ptr<hb::HeartbeatService::Stub>>         hb_stubs_;
    size_t biz_idx_{0};
    size_t hb_idx_{0};
};

int main(){
    std::string ip = GetLocalIPAddress();
    std::cout << "Local IP : " << ip << '\n';

    std::vector<std::string> biz={
        "192.168.188.150:50051",   // A
        "192.168.188.236:50051",    // C
        "192.168.0.196:50051"   // B

    };
    std::vector<std::string> hb={
        "192.168.188.150:50052",   // A
        "192.168.188.236:50052",    // C
        "192.168.0.196:50052"
    };

    RegistryClient cli(biz, hb);

    const std::string name = "Cpp_Calculator";
    const std::string addr = ip + ":8000";
    const std::string lang = "C++", ver = "1.0", region = "PL";

    if (!cli.Register(name, addr, lang, ver, region)) {
        std::cerr << "Register failed\n";
        return 1;
    }
    std::cout << "Registered.\n";

    std::atomic<bool> running{true}, alive{true};
    std::thread t([&]{
        while (running) {
            std::this_thread::sleep_for(std::chrono::seconds(3));
            if (alive)
                cli.ensureAlive(name, addr, lang, ver, region);
        }
    });

    std::cout << "\nCommands: 1=Lookup  2=Deregister  3=Re-Register  4=Exit\n";
    while (true) {
        if (!_kbhit()) continue;
        char c = _getch();
        if (c == '1') {
            auto r = cli.Lookup(name);
            std::cout << "Lookup -> " << (r.empty() ? "(not found)" : r) << '\n';
        } else if (c == '2') {
            if (cli.Deregister(name)) { alive = false; std::cout << "Deregister ok\n"; }
            else std::cout << "Deregister failed\n";
        } else if (c == '3') {
            if (cli.Register(name, addr, lang, ver, region)) { alive = true; std::cout << "Re-Register ok\n"; }
            else std::cout << "Re-Register failed\n";
        } else if (c == '4') break;
    }
    running = false; t.join();
    if (alive) cli.Deregister(name);
    return 0;
}
