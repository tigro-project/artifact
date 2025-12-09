package tigro;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * No KeyGen protocol since server does not participate in KeyGen.
 * No explicit Init protocol since the server does it on its own at initial startup.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: tigro.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TigroServiceGrpc {

  private TigroServiceGrpc() {}

  public static final String SERVICE_NAME = "tigro.TigroService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<tigro.Tigro.HelloRequest,
      tigro.Tigro.HelloReply> getHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Hello",
      requestType = tigro.Tigro.HelloRequest.class,
      responseType = tigro.Tigro.HelloReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tigro.Tigro.HelloRequest,
      tigro.Tigro.HelloReply> getHelloMethod() {
    io.grpc.MethodDescriptor<tigro.Tigro.HelloRequest, tigro.Tigro.HelloReply> getHelloMethod;
    if ((getHelloMethod = TigroServiceGrpc.getHelloMethod) == null) {
      synchronized (TigroServiceGrpc.class) {
        if ((getHelloMethod = TigroServiceGrpc.getHelloMethod) == null) {
          TigroServiceGrpc.getHelloMethod = getHelloMethod =
              io.grpc.MethodDescriptor.<tigro.Tigro.HelloRequest, tigro.Tigro.HelloReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Hello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.HelloReply.getDefaultInstance()))
              .setSchemaDescriptor(new TigroServiceMethodDescriptorSupplier("Hello"))
              .build();
        }
      }
    }
    return getHelloMethod;
  }

  private static volatile io.grpc.MethodDescriptor<tigro.Tigro.CreatePOBoxRequest,
      tigro.Tigro.CreatePOBoxReply> getCreatePOBoxMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreatePOBox",
      requestType = tigro.Tigro.CreatePOBoxRequest.class,
      responseType = tigro.Tigro.CreatePOBoxReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tigro.Tigro.CreatePOBoxRequest,
      tigro.Tigro.CreatePOBoxReply> getCreatePOBoxMethod() {
    io.grpc.MethodDescriptor<tigro.Tigro.CreatePOBoxRequest, tigro.Tigro.CreatePOBoxReply> getCreatePOBoxMethod;
    if ((getCreatePOBoxMethod = TigroServiceGrpc.getCreatePOBoxMethod) == null) {
      synchronized (TigroServiceGrpc.class) {
        if ((getCreatePOBoxMethod = TigroServiceGrpc.getCreatePOBoxMethod) == null) {
          TigroServiceGrpc.getCreatePOBoxMethod = getCreatePOBoxMethod =
              io.grpc.MethodDescriptor.<tigro.Tigro.CreatePOBoxRequest, tigro.Tigro.CreatePOBoxReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreatePOBox"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.CreatePOBoxRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.CreatePOBoxReply.getDefaultInstance()))
              .setSchemaDescriptor(new TigroServiceMethodDescriptorSupplier("CreatePOBox"))
              .build();
        }
      }
    }
    return getCreatePOBoxMethod;
  }

  private static volatile io.grpc.MethodDescriptor<tigro.Tigro.DropMailRequest,
      tigro.Tigro.DropMailReply> getDropMailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropMail",
      requestType = tigro.Tigro.DropMailRequest.class,
      responseType = tigro.Tigro.DropMailReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tigro.Tigro.DropMailRequest,
      tigro.Tigro.DropMailReply> getDropMailMethod() {
    io.grpc.MethodDescriptor<tigro.Tigro.DropMailRequest, tigro.Tigro.DropMailReply> getDropMailMethod;
    if ((getDropMailMethod = TigroServiceGrpc.getDropMailMethod) == null) {
      synchronized (TigroServiceGrpc.class) {
        if ((getDropMailMethod = TigroServiceGrpc.getDropMailMethod) == null) {
          TigroServiceGrpc.getDropMailMethod = getDropMailMethod =
              io.grpc.MethodDescriptor.<tigro.Tigro.DropMailRequest, tigro.Tigro.DropMailReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DropMail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.DropMailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.DropMailReply.getDefaultInstance()))
              .setSchemaDescriptor(new TigroServiceMethodDescriptorSupplier("DropMail"))
              .build();
        }
      }
    }
    return getDropMailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<tigro.Tigro.GetMailRequest,
      tigro.Tigro.GetMailReply> getGetMailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMail",
      requestType = tigro.Tigro.GetMailRequest.class,
      responseType = tigro.Tigro.GetMailReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tigro.Tigro.GetMailRequest,
      tigro.Tigro.GetMailReply> getGetMailMethod() {
    io.grpc.MethodDescriptor<tigro.Tigro.GetMailRequest, tigro.Tigro.GetMailReply> getGetMailMethod;
    if ((getGetMailMethod = TigroServiceGrpc.getGetMailMethod) == null) {
      synchronized (TigroServiceGrpc.class) {
        if ((getGetMailMethod = TigroServiceGrpc.getGetMailMethod) == null) {
          TigroServiceGrpc.getGetMailMethod = getGetMailMethod =
              io.grpc.MethodDescriptor.<tigro.Tigro.GetMailRequest, tigro.Tigro.GetMailReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.GetMailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.GetMailReply.getDefaultInstance()))
              .setSchemaDescriptor(new TigroServiceMethodDescriptorSupplier("GetMail"))
              .build();
        }
      }
    }
    return getGetMailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<tigro.Tigro.DeleteMailRequest,
      tigro.Tigro.DeleteMailReply> getDeleteMailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteMail",
      requestType = tigro.Tigro.DeleteMailRequest.class,
      responseType = tigro.Tigro.DeleteMailReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<tigro.Tigro.DeleteMailRequest,
      tigro.Tigro.DeleteMailReply> getDeleteMailMethod() {
    io.grpc.MethodDescriptor<tigro.Tigro.DeleteMailRequest, tigro.Tigro.DeleteMailReply> getDeleteMailMethod;
    if ((getDeleteMailMethod = TigroServiceGrpc.getDeleteMailMethod) == null) {
      synchronized (TigroServiceGrpc.class) {
        if ((getDeleteMailMethod = TigroServiceGrpc.getDeleteMailMethod) == null) {
          TigroServiceGrpc.getDeleteMailMethod = getDeleteMailMethod =
              io.grpc.MethodDescriptor.<tigro.Tigro.DeleteMailRequest, tigro.Tigro.DeleteMailReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteMail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.DeleteMailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  tigro.Tigro.DeleteMailReply.getDefaultInstance()))
              .setSchemaDescriptor(new TigroServiceMethodDescriptorSupplier("DeleteMail"))
              .build();
        }
      }
    }
    return getDeleteMailMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TigroServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TigroServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TigroServiceStub>() {
        @java.lang.Override
        public TigroServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TigroServiceStub(channel, callOptions);
        }
      };
    return TigroServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TigroServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TigroServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TigroServiceBlockingStub>() {
        @java.lang.Override
        public TigroServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TigroServiceBlockingStub(channel, callOptions);
        }
      };
    return TigroServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TigroServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TigroServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TigroServiceFutureStub>() {
        @java.lang.Override
        public TigroServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TigroServiceFutureStub(channel, callOptions);
        }
      };
    return TigroServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static abstract class TigroServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public void hello(tigro.Tigro.HelloRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.HelloReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloMethod(), responseObserver);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public void createPOBox(tigro.Tigro.CreatePOBoxRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.CreatePOBoxReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreatePOBoxMethod(), responseObserver);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public void dropMail(tigro.Tigro.DropMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DropMailReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDropMailMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void getMail(tigro.Tigro.GetMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.GetMailReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMailMethod(), responseObserver);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void deleteMail(tigro.Tigro.DeleteMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DeleteMailReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMailMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHelloMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.HelloRequest,
                tigro.Tigro.HelloReply>(
                  this, METHODID_HELLO)))
          .addMethod(
            getCreatePOBoxMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.CreatePOBoxRequest,
                tigro.Tigro.CreatePOBoxReply>(
                  this, METHODID_CREATE_POBOX)))
          .addMethod(
            getDropMailMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.DropMailRequest,
                tigro.Tigro.DropMailReply>(
                  this, METHODID_DROP_MAIL)))
          .addMethod(
            getGetMailMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.GetMailRequest,
                tigro.Tigro.GetMailReply>(
                  this, METHODID_GET_MAIL)))
          .addMethod(
            getDeleteMailMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.DeleteMailRequest,
                tigro.Tigro.DeleteMailReply>(
                  this, METHODID_DELETE_MAIL)))
          .build();
    }
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static final class TigroServiceStub extends io.grpc.stub.AbstractAsyncStub<TigroServiceStub> {
    private TigroServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TigroServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public void hello(tigro.Tigro.HelloRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.HelloReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public void createPOBox(tigro.Tigro.CreatePOBoxRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.CreatePOBoxReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreatePOBoxMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public void dropMail(tigro.Tigro.DropMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DropMailReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDropMailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void getMail(tigro.Tigro.GetMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.GetMailReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void deleteMail(tigro.Tigro.DeleteMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DeleteMailReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMailMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static final class TigroServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<TigroServiceBlockingStub> {
    private TigroServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TigroServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public tigro.Tigro.HelloReply hello(tigro.Tigro.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHelloMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public tigro.Tigro.CreatePOBoxReply createPOBox(tigro.Tigro.CreatePOBoxRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreatePOBoxMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.DropMailReply dropMail(tigro.Tigro.DropMailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDropMailMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.GetMailReply getMail(tigro.Tigro.GetMailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMailMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.DeleteMailReply deleteMail(tigro.Tigro.DeleteMailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMailMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static final class TigroServiceFutureStub extends io.grpc.stub.AbstractFutureStub<TigroServiceFutureStub> {
    private TigroServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TigroServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.HelloReply> hello(
        tigro.Tigro.HelloRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.CreatePOBoxReply> createPOBox(
        tigro.Tigro.CreatePOBoxRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreatePOBoxMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.DropMailReply> dropMail(
        tigro.Tigro.DropMailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDropMailMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.GetMailReply> getMail(
        tigro.Tigro.GetMailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMailMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.DeleteMailReply> deleteMail(
        tigro.Tigro.DeleteMailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMailMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HELLO = 0;
  private static final int METHODID_CREATE_POBOX = 1;
  private static final int METHODID_DROP_MAIL = 2;
  private static final int METHODID_GET_MAIL = 3;
  private static final int METHODID_DELETE_MAIL = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TigroServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TigroServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO:
          serviceImpl.hello((tigro.Tigro.HelloRequest) request,
              (io.grpc.stub.StreamObserver<tigro.Tigro.HelloReply>) responseObserver);
          break;
        case METHODID_CREATE_POBOX:
          serviceImpl.createPOBox((tigro.Tigro.CreatePOBoxRequest) request,
              (io.grpc.stub.StreamObserver<tigro.Tigro.CreatePOBoxReply>) responseObserver);
          break;
        case METHODID_DROP_MAIL:
          serviceImpl.dropMail((tigro.Tigro.DropMailRequest) request,
              (io.grpc.stub.StreamObserver<tigro.Tigro.DropMailReply>) responseObserver);
          break;
        case METHODID_GET_MAIL:
          serviceImpl.getMail((tigro.Tigro.GetMailRequest) request,
              (io.grpc.stub.StreamObserver<tigro.Tigro.GetMailReply>) responseObserver);
          break;
        case METHODID_DELETE_MAIL:
          serviceImpl.deleteMail((tigro.Tigro.DeleteMailRequest) request,
              (io.grpc.stub.StreamObserver<tigro.Tigro.DeleteMailReply>) responseObserver);
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

  private static abstract class TigroServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TigroServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return tigro.Tigro.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TigroService");
    }
  }

  private static final class TigroServiceFileDescriptorSupplier
      extends TigroServiceBaseDescriptorSupplier {
    TigroServiceFileDescriptorSupplier() {}
  }

  private static final class TigroServiceMethodDescriptorSupplier
      extends TigroServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TigroServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TigroServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TigroServiceFileDescriptorSupplier())
              .addMethod(getHelloMethod())
              .addMethod(getCreatePOBoxMethod())
              .addMethod(getDropMailMethod())
              .addMethod(getGetMailMethod())
              .addMethod(getDeleteMailMethod())
              .build();
        }
      }
    }
    return result;
  }
}
