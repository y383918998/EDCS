// File: HeartbeatServiceImpl.java
package objectrepo;

import com.google.protobuf.Empty;
import objectrepo.Config;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import hb.HeartbeatServiceGrpc.HeartbeatServiceImplBase;
import objectrepo.ObjectRepositoryImpl;
import hb.HeartbeatProto.*;

import java.io.IOException;
import java.time.Instant;

public class HeartbeatServiceImpl extends HeartbeatServiceImplBase {
    private final ObjectRepositoryImpl registry;
    private final long startTime = System.currentTimeMillis();

    public HeartbeatServiceImpl(ObjectRepositoryImpl r) {
        this.registry = r;
    }

    @Override
    public void ping(Empty request, StreamObserver<Empty> responseObserver) {
        if (registry.isLeader()) {
            System.out.println("[PING] " + Instant.now() + " from client");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.UNAVAILABLE.withDescription("not primary").asRuntimeException());
        }
    }

    @Override
    public void getUptime(Empty request, StreamObserver<UptimeInfo> responseObserver) {
        long uptime = (System.currentTimeMillis() - startTime) / 1000;

        try {
            String nodeId = Config.load("config.json").node_id;
            UptimeInfo info = UptimeInfo.newBuilder()
                    .setNodeId(nodeId)
                    .setUptimeSec(uptime)
                    .build();
            responseObserver.onNext(info);
            responseObserver.onCompleted();
        } catch (IOException e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to read config: " + e.getMessage())
                    .asRuntimeException());
        }
    }

}
