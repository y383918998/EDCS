package org.example.demo;

import objectrepo.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class RegistryDemo {
    public static void main(String[] args) {
        //
        String[] serverIps = {"192.168.5.44","192.168.5.42"};
        int serverPort = 50051;

        ManagedChannel channel = null;
        ObjectRepositoryGrpc.ObjectRepositoryBlockingStub stub = null;

        //
        for (String serverIp : serverIps) {
            try {
                channel = ManagedChannelBuilder
                        .forAddress(serverIp, serverPort)
                        .usePlaintext()
                        .build();
                stub = ObjectRepositoryGrpc.newBlockingStub(channel);

                // 发送简单心跳确认是否连接成功
                stub.heartbeat(HeartbeatPing.newBuilder().setObjectName("test").build());
                System.out.println(" Successfully connected to the server: " + serverIp);
                break;
            } catch (Exception e) {
                System.out.println(" Unable to connect to the server: " + serverIp);
                if (channel != null) channel.shutdownNow();
            }
        }

        if (stub == null) {
            System.out.println("All servers are unavailable!");
            return;
        }

        // 后续业务逻辑：
        String objectName = "Java_Calculator";
        String myObjectAddress = "192.168.5.42:8000";

        try {
            RegisterResponse reg = stub.registerObject(
                    RegisterRequest.newBuilder()
                            .setObjectName(objectName)
                            .setObjectAddress(myObjectAddress)
                            .setLanguage("Java")
                            .setVersion("1.0")
                            .setRegion("PL")
                            .setIsReplication(false)
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
        } catch (StatusRuntimeException e) {
            System.out.println("Call failed：" + e.getMessage());
        } finally {
            channel.shutdownNow();
        }
    }
}
