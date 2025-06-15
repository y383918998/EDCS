package org.example.demo;

import objectrepo.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RegistryDemo {
    public static void main(String[] args) {

        String serverIp = "192.168.5.44";
        int serverPort = 50051;

        ManagedChannel ch = ManagedChannelBuilder
                .forAddress(serverIp, serverPort)
                .usePlaintext()
                .build();

        ObjectRepositoryGrpc.ObjectRepositoryBlockingStub stub =
                ObjectRepositoryGrpc.newBlockingStub(ch);

        // ✅ 改为统一使用相同的 objectName，避免 lookup 失败
        String objectName = "Java_Calculator";
        String myObjectAddress = "192.168.5.42:8000";

        RegisterResponse reg = stub.registerObject(
            RegisterRequest.newBuilder()
                            .setObjectName(objectName)
                            .setObjectAddress(myObjectAddress)
                            .setLanguage("Java")
                            .setVersion("1.0")
                            .setRegion("PL")
                            .build());
        System.out.println("Registered? " + reg.getSuccess());

        String addr = stub.getObject(
                GetRequest.newBuilder().setObjectName(objectName).build())
                .getObjectAddress();
        System.out.println("Lookup -> " + addr);

        boolean ok = stub.heartbeat(
                HeartbeatPing.newBuilder().setObjectName(objectName).build())
                .getOk();
        System.out.println("Heartbeat ok? " + ok);

        DeregisterResponse de = stub.deregisterObject(
                DeregisterRequest.newBuilder().setObjectName(objectName).build());
        System.out.println("Deregistered? " + de.getSuccess());

        ch.shutdownNow();
    }
}
