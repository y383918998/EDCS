package org.example.demo;

import objectrepo.*;                            // ← 改这一行

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RegistryDemo {
    public static void main(String[] args) {

        ManagedChannel ch = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // 注意包名前缀也要换成 objectrepo
        ObjectRepositoryGrpc.ObjectRepositoryBlockingStub stub =
                ObjectRepositoryGrpc.newBlockingStub(ch);

        /* -------- Register -------- */
        RegisterResponse reg = stub.registerObject(
            RegisterRequest.newBuilder()
                            .setObjectName("Calculator")
                            .setObjectAddress("127.0.0.1:7000")
                            .setLanguage("Java")
                            .setVersion("1.0")
                            .setRegion("CN")
                            .build());
        System.out.println("Registered? " + reg.getSuccess());

        /* -------- Lookup -------- */
        String addr = stub.getObject(
                GetRequest.newBuilder().setObjectName("Calculator").build())
                .getObjectAddress();
        System.out.println("Lookup -> " + addr);

        /* -------- Heartbeat -------- */
        boolean ok = stub.heartbeat(
                HeartbeatPing.newBuilder().setObjectName("Calculator").build())
                .getOk();
        System.out.println("Heartbeat ok? " + ok);

        /* -------- Deregister -------- */
        DeregisterResponse de = stub.deregisterObject(
                DeregisterRequest.newBuilder().setObjectName("Calculator").build());
        System.out.println("Deregistered? " + de.getSuccess());

        ch.shutdownNow();
    }
}
