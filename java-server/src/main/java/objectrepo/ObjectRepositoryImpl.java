// File: ObjectRepositoryImpl.java
package objectrepo;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import objectrepo.ObjectRepositoryProto.*;


public class ObjectRepositoryImpl extends ObjectRepositoryGrpc.ObjectRepositoryImplBase {
    private final RegistryDatabase db;
    private boolean isLeader = false;

    public ObjectRepositoryImpl(long ttlSeconds) {
        this.db = new RegistryDatabase(ttlSeconds);
        System.out.println("ObjectRepositoryImpl init done.");
    }

    public void setLeader(boolean leader) {
        this.isLeader = leader;
        System.out.println("[ROLE] now " + (leader ? "PRIMARY" : "BACKUP"));
    }

    public boolean isLeader() {
        return this.isLeader;
    }

    @Override
    public void registerObject(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        ObjectInfo info = ObjectInfo.newBuilder()
                .setObjectName(request.getObjectName())
                .setObjectAddress(request.getObjectAddress())
                .setLanguage(request.getLanguage())
                .setVersion(request.getVersion())
                .setRegion(request.getRegion())
                .build();

        boolean success = db.register(info);

        RegisterResponse response = RegisterResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deregisterObject(DeregisterRequest request, StreamObserver<DeregisterResponse> responseObserver) {
        boolean success = db.deregister(request.getObjectName());

        DeregisterResponse response = DeregisterResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateObject(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        ObjectInfo info = ObjectInfo.newBuilder()
                .setObjectName(request.getObjectName())
                .setObjectAddress(request.getObjectAddress())
                .setLanguage(request.getLanguage())
                .setVersion(request.getVersion())
                .setRegion(request.getRegion())
                .build();

        boolean success = db.update(info);

        UpdateResponse response = UpdateResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getObject(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        ObjectInfo info = db.get(request.getObjectName());

        GetResponse.Builder builder = GetResponse.newBuilder();
        if (info != null) {
            builder.setObjectAddress(info.getObjectAddress());
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void listObjects(Empty request, StreamObserver<ObjectListResponse> responseObserver) {
        ObjectListResponse.Builder builder = ObjectListResponse.newBuilder();
        builder.addAllObjects(db.listAll());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void heartbeat(HeartbeatPing request, StreamObserver<HeartbeatAck> responseObserver) {
        boolean ok = db.heartbeat(request.getObjectName());
        HeartbeatAck response = HeartbeatAck.newBuilder().setOk(ok).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void syncState(ObjectListResponse request, StreamObserver<Empty> responseObserver) {
        db.syncFrom(request.getObjectsList());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
