package objectrepo;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ---------- Service ----------
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: object_repository.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ObjectRepositoryGrpc {

  private ObjectRepositoryGrpc() {}

  public static final java.lang.String SERVICE_NAME = "objectrepo.ObjectRepository";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<objectrepo.RegisterRequest,
      objectrepo.RegisterResponse> getRegisterObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterObject",
      requestType = objectrepo.RegisterRequest.class,
      responseType = objectrepo.RegisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<objectrepo.RegisterRequest,
      objectrepo.RegisterResponse> getRegisterObjectMethod() {
    io.grpc.MethodDescriptor<objectrepo.RegisterRequest, objectrepo.RegisterResponse> getRegisterObjectMethod;
    if ((getRegisterObjectMethod = ObjectRepositoryGrpc.getRegisterObjectMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getRegisterObjectMethod = ObjectRepositoryGrpc.getRegisterObjectMethod) == null) {
          ObjectRepositoryGrpc.getRegisterObjectMethod = getRegisterObjectMethod =
              io.grpc.MethodDescriptor.<objectrepo.RegisterRequest, objectrepo.RegisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.RegisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.RegisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("RegisterObject"))
              .build();
        }
      }
    }
    return getRegisterObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<objectrepo.DeregisterRequest,
      objectrepo.DeregisterResponse> getDeregisterObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeregisterObject",
      requestType = objectrepo.DeregisterRequest.class,
      responseType = objectrepo.DeregisterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<objectrepo.DeregisterRequest,
      objectrepo.DeregisterResponse> getDeregisterObjectMethod() {
    io.grpc.MethodDescriptor<objectrepo.DeregisterRequest, objectrepo.DeregisterResponse> getDeregisterObjectMethod;
    if ((getDeregisterObjectMethod = ObjectRepositoryGrpc.getDeregisterObjectMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getDeregisterObjectMethod = ObjectRepositoryGrpc.getDeregisterObjectMethod) == null) {
          ObjectRepositoryGrpc.getDeregisterObjectMethod = getDeregisterObjectMethod =
              io.grpc.MethodDescriptor.<objectrepo.DeregisterRequest, objectrepo.DeregisterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeregisterObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.DeregisterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.DeregisterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("DeregisterObject"))
              .build();
        }
      }
    }
    return getDeregisterObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<objectrepo.UpdateRequest,
      objectrepo.UpdateResponse> getUpdateObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateObject",
      requestType = objectrepo.UpdateRequest.class,
      responseType = objectrepo.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<objectrepo.UpdateRequest,
      objectrepo.UpdateResponse> getUpdateObjectMethod() {
    io.grpc.MethodDescriptor<objectrepo.UpdateRequest, objectrepo.UpdateResponse> getUpdateObjectMethod;
    if ((getUpdateObjectMethod = ObjectRepositoryGrpc.getUpdateObjectMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getUpdateObjectMethod = ObjectRepositoryGrpc.getUpdateObjectMethod) == null) {
          ObjectRepositoryGrpc.getUpdateObjectMethod = getUpdateObjectMethod =
              io.grpc.MethodDescriptor.<objectrepo.UpdateRequest, objectrepo.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("UpdateObject"))
              .build();
        }
      }
    }
    return getUpdateObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<objectrepo.GetRequest,
      objectrepo.GetResponse> getGetObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetObject",
      requestType = objectrepo.GetRequest.class,
      responseType = objectrepo.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<objectrepo.GetRequest,
      objectrepo.GetResponse> getGetObjectMethod() {
    io.grpc.MethodDescriptor<objectrepo.GetRequest, objectrepo.GetResponse> getGetObjectMethod;
    if ((getGetObjectMethod = ObjectRepositoryGrpc.getGetObjectMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getGetObjectMethod = ObjectRepositoryGrpc.getGetObjectMethod) == null) {
          ObjectRepositoryGrpc.getGetObjectMethod = getGetObjectMethod =
              io.grpc.MethodDescriptor.<objectrepo.GetRequest, objectrepo.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.GetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("GetObject"))
              .build();
        }
      }
    }
    return getGetObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      objectrepo.ObjectListResponse> getListObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListObjects",
      requestType = com.google.protobuf.Empty.class,
      responseType = objectrepo.ObjectListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      objectrepo.ObjectListResponse> getListObjectsMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, objectrepo.ObjectListResponse> getListObjectsMethod;
    if ((getListObjectsMethod = ObjectRepositoryGrpc.getListObjectsMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getListObjectsMethod = ObjectRepositoryGrpc.getListObjectsMethod) == null) {
          ObjectRepositoryGrpc.getListObjectsMethod = getListObjectsMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, objectrepo.ObjectListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.ObjectListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("ListObjects"))
              .build();
        }
      }
    }
    return getListObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<objectrepo.HeartbeatPing,
      objectrepo.HeartbeatAck> getHeartbeatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Heartbeat",
      requestType = objectrepo.HeartbeatPing.class,
      responseType = objectrepo.HeartbeatAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<objectrepo.HeartbeatPing,
      objectrepo.HeartbeatAck> getHeartbeatMethod() {
    io.grpc.MethodDescriptor<objectrepo.HeartbeatPing, objectrepo.HeartbeatAck> getHeartbeatMethod;
    if ((getHeartbeatMethod = ObjectRepositoryGrpc.getHeartbeatMethod) == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        if ((getHeartbeatMethod = ObjectRepositoryGrpc.getHeartbeatMethod) == null) {
          ObjectRepositoryGrpc.getHeartbeatMethod = getHeartbeatMethod =
              io.grpc.MethodDescriptor.<objectrepo.HeartbeatPing, objectrepo.HeartbeatAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Heartbeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.HeartbeatPing.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  objectrepo.HeartbeatAck.getDefaultInstance()))
              .setSchemaDescriptor(new ObjectRepositoryMethodDescriptorSupplier("Heartbeat"))
              .build();
        }
      }
    }
    return getHeartbeatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ObjectRepositoryStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryStub>() {
        @java.lang.Override
        public ObjectRepositoryStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ObjectRepositoryStub(channel, callOptions);
        }
      };
    return ObjectRepositoryStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ObjectRepositoryBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryBlockingStub>() {
        @java.lang.Override
        public ObjectRepositoryBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ObjectRepositoryBlockingStub(channel, callOptions);
        }
      };
    return ObjectRepositoryBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ObjectRepositoryFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ObjectRepositoryFutureStub>() {
        @java.lang.Override
        public ObjectRepositoryFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ObjectRepositoryFutureStub(channel, callOptions);
        }
      };
    return ObjectRepositoryFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ---------- Service ----------
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void registerObject(objectrepo.RegisterRequest request,
        io.grpc.stub.StreamObserver<objectrepo.RegisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterObjectMethod(), responseObserver);
    }

    /**
     */
    default void deregisterObject(objectrepo.DeregisterRequest request,
        io.grpc.stub.StreamObserver<objectrepo.DeregisterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeregisterObjectMethod(), responseObserver);
    }

    /**
     */
    default void updateObject(objectrepo.UpdateRequest request,
        io.grpc.stub.StreamObserver<objectrepo.UpdateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateObjectMethod(), responseObserver);
    }

    /**
     */
    default void getObject(objectrepo.GetRequest request,
        io.grpc.stub.StreamObserver<objectrepo.GetResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetObjectMethod(), responseObserver);
    }

    /**
     */
    default void listObjects(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<objectrepo.ObjectListResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListObjectsMethod(), responseObserver);
    }

    /**
     */
    default void heartbeat(objectrepo.HeartbeatPing request,
        io.grpc.stub.StreamObserver<objectrepo.HeartbeatAck> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHeartbeatMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ObjectRepository.
   * <pre>
   * ---------- Service ----------
   * </pre>
   */
  public static abstract class ObjectRepositoryImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ObjectRepositoryGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ObjectRepository.
   * <pre>
   * ---------- Service ----------
   * </pre>
   */
  public static final class ObjectRepositoryStub
      extends io.grpc.stub.AbstractAsyncStub<ObjectRepositoryStub> {
    private ObjectRepositoryStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ObjectRepositoryStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ObjectRepositoryStub(channel, callOptions);
    }

    /**
     */
    public void registerObject(objectrepo.RegisterRequest request,
        io.grpc.stub.StreamObserver<objectrepo.RegisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deregisterObject(objectrepo.DeregisterRequest request,
        io.grpc.stub.StreamObserver<objectrepo.DeregisterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeregisterObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateObject(objectrepo.UpdateRequest request,
        io.grpc.stub.StreamObserver<objectrepo.UpdateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObject(objectrepo.GetRequest request,
        io.grpc.stub.StreamObserver<objectrepo.GetResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listObjects(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<objectrepo.ObjectListResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void heartbeat(objectrepo.HeartbeatPing request,
        io.grpc.stub.StreamObserver<objectrepo.HeartbeatAck> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHeartbeatMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ObjectRepository.
   * <pre>
   * ---------- Service ----------
   * </pre>
   */
  public static final class ObjectRepositoryBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ObjectRepositoryBlockingStub> {
    private ObjectRepositoryBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ObjectRepositoryBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ObjectRepositoryBlockingStub(channel, callOptions);
    }

    /**
     */
    public objectrepo.RegisterResponse registerObject(objectrepo.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public objectrepo.DeregisterResponse deregisterObject(objectrepo.DeregisterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeregisterObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public objectrepo.UpdateResponse updateObject(objectrepo.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public objectrepo.GetResponse getObject(objectrepo.GetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public objectrepo.ObjectListResponse listObjects(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public objectrepo.HeartbeatAck heartbeat(objectrepo.HeartbeatPing request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHeartbeatMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ObjectRepository.
   * <pre>
   * ---------- Service ----------
   * </pre>
   */
  public static final class ObjectRepositoryFutureStub
      extends io.grpc.stub.AbstractFutureStub<ObjectRepositoryFutureStub> {
    private ObjectRepositoryFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ObjectRepositoryFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ObjectRepositoryFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.RegisterResponse> registerObject(
        objectrepo.RegisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.DeregisterResponse> deregisterObject(
        objectrepo.DeregisterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeregisterObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.UpdateResponse> updateObject(
        objectrepo.UpdateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.GetResponse> getObject(
        objectrepo.GetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.ObjectListResponse> listObjects(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<objectrepo.HeartbeatAck> heartbeat(
        objectrepo.HeartbeatPing request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHeartbeatMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER_OBJECT = 0;
  private static final int METHODID_DEREGISTER_OBJECT = 1;
  private static final int METHODID_UPDATE_OBJECT = 2;
  private static final int METHODID_GET_OBJECT = 3;
  private static final int METHODID_LIST_OBJECTS = 4;
  private static final int METHODID_HEARTBEAT = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER_OBJECT:
          serviceImpl.registerObject((objectrepo.RegisterRequest) request,
              (io.grpc.stub.StreamObserver<objectrepo.RegisterResponse>) responseObserver);
          break;
        case METHODID_DEREGISTER_OBJECT:
          serviceImpl.deregisterObject((objectrepo.DeregisterRequest) request,
              (io.grpc.stub.StreamObserver<objectrepo.DeregisterResponse>) responseObserver);
          break;
        case METHODID_UPDATE_OBJECT:
          serviceImpl.updateObject((objectrepo.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<objectrepo.UpdateResponse>) responseObserver);
          break;
        case METHODID_GET_OBJECT:
          serviceImpl.getObject((objectrepo.GetRequest) request,
              (io.grpc.stub.StreamObserver<objectrepo.GetResponse>) responseObserver);
          break;
        case METHODID_LIST_OBJECTS:
          serviceImpl.listObjects((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<objectrepo.ObjectListResponse>) responseObserver);
          break;
        case METHODID_HEARTBEAT:
          serviceImpl.heartbeat((objectrepo.HeartbeatPing) request,
              (io.grpc.stub.StreamObserver<objectrepo.HeartbeatAck>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getRegisterObjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              objectrepo.RegisterRequest,
              objectrepo.RegisterResponse>(
                service, METHODID_REGISTER_OBJECT)))
        .addMethod(
          getDeregisterObjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              objectrepo.DeregisterRequest,
              objectrepo.DeregisterResponse>(
                service, METHODID_DEREGISTER_OBJECT)))
        .addMethod(
          getUpdateObjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              objectrepo.UpdateRequest,
              objectrepo.UpdateResponse>(
                service, METHODID_UPDATE_OBJECT)))
        .addMethod(
          getGetObjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              objectrepo.GetRequest,
              objectrepo.GetResponse>(
                service, METHODID_GET_OBJECT)))
        .addMethod(
          getListObjectsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              objectrepo.ObjectListResponse>(
                service, METHODID_LIST_OBJECTS)))
        .addMethod(
          getHeartbeatMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              objectrepo.HeartbeatPing,
              objectrepo.HeartbeatAck>(
                service, METHODID_HEARTBEAT)))
        .build();
  }

  private static abstract class ObjectRepositoryBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ObjectRepositoryBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return objectrepo.ObjectRepositoryOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ObjectRepository");
    }
  }

  private static final class ObjectRepositoryFileDescriptorSupplier
      extends ObjectRepositoryBaseDescriptorSupplier {
    ObjectRepositoryFileDescriptorSupplier() {}
  }

  private static final class ObjectRepositoryMethodDescriptorSupplier
      extends ObjectRepositoryBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ObjectRepositoryMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ObjectRepositoryGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ObjectRepositoryFileDescriptorSupplier())
              .addMethod(getRegisterObjectMethod())
              .addMethod(getDeregisterObjectMethod())
              .addMethod(getUpdateObjectMethod())
              .addMethod(getGetObjectMethod())
              .addMethod(getListObjectsMethod())
              .addMethod(getHeartbeatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
