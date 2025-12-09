package tigro;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * No KeyGen protocol since server does not participate in KeyGen.
 * No explicit Init protocol since the server does it on its own at initial startup.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: tigro.proto")
public final class TigroServiceGrpc {

  private TigroServiceGrpc() {}

  public static final String SERVICE_NAME = "tigro.TigroService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<tigro.Tigro.HelloRequest,
      tigro.Tigro.HelloReply> METHOD_HELLO =
      io.grpc.MethodDescriptor.<tigro.Tigro.HelloRequest, tigro.Tigro.HelloReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "tigro.TigroService", "Hello"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.HelloRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.HelloReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<tigro.Tigro.CreatePOBoxRequest,
      tigro.Tigro.CreatePOBoxReply> METHOD_CREATE_POBOX =
      io.grpc.MethodDescriptor.<tigro.Tigro.CreatePOBoxRequest, tigro.Tigro.CreatePOBoxReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "tigro.TigroService", "CreatePOBox"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.CreatePOBoxRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.CreatePOBoxReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<tigro.Tigro.DropMailRequest,
      tigro.Tigro.DropMailReply> METHOD_DROP_MAIL =
      io.grpc.MethodDescriptor.<tigro.Tigro.DropMailRequest, tigro.Tigro.DropMailReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "tigro.TigroService", "DropMail"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.DropMailRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.DropMailReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<tigro.Tigro.GetMailRequest,
      tigro.Tigro.GetMailReply> METHOD_GET_MAIL =
      io.grpc.MethodDescriptor.<tigro.Tigro.GetMailRequest, tigro.Tigro.GetMailReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "tigro.TigroService", "GetMail"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.GetMailRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.GetMailReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<tigro.Tigro.DeleteMailRequest,
      tigro.Tigro.DeleteMailReply> METHOD_DELETE_MAIL =
      io.grpc.MethodDescriptor.<tigro.Tigro.DeleteMailRequest, tigro.Tigro.DeleteMailReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "tigro.TigroService", "DeleteMail"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.DeleteMailRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              tigro.Tigro.DeleteMailReply.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TigroServiceStub newStub(io.grpc.Channel channel) {
    return new TigroServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TigroServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TigroServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TigroServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TigroServiceFutureStub(channel);
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
      asyncUnimplementedUnaryCall(METHOD_HELLO, responseObserver);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public void createPOBox(tigro.Tigro.CreatePOBoxRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.CreatePOBoxReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_POBOX, responseObserver);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public void dropMail(tigro.Tigro.DropMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DropMailReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DROP_MAIL, responseObserver);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void getMail(tigro.Tigro.GetMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.GetMailReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_MAIL, responseObserver);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void deleteMail(tigro.Tigro.DeleteMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DeleteMailReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_MAIL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_HELLO,
            asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.HelloRequest,
                tigro.Tigro.HelloReply>(
                  this, METHODID_HELLO)))
          .addMethod(
            METHOD_CREATE_POBOX,
            asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.CreatePOBoxRequest,
                tigro.Tigro.CreatePOBoxReply>(
                  this, METHODID_CREATE_POBOX)))
          .addMethod(
            METHOD_DROP_MAIL,
            asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.DropMailRequest,
                tigro.Tigro.DropMailReply>(
                  this, METHODID_DROP_MAIL)))
          .addMethod(
            METHOD_GET_MAIL,
            asyncUnaryCall(
              new MethodHandlers<
                tigro.Tigro.GetMailRequest,
                tigro.Tigro.GetMailReply>(
                  this, METHODID_GET_MAIL)))
          .addMethod(
            METHOD_DELETE_MAIL,
            asyncUnaryCall(
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
  public static final class TigroServiceStub extends io.grpc.stub.AbstractStub<TigroServiceStub> {
    private TigroServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TigroServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TigroServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public void hello(tigro.Tigro.HelloRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.HelloReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public void createPOBox(tigro.Tigro.CreatePOBoxRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.CreatePOBoxReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREATE_POBOX, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public void dropMail(tigro.Tigro.DropMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DropMailReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DROP_MAIL, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void getMail(tigro.Tigro.GetMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.GetMailReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_MAIL, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public void deleteMail(tigro.Tigro.DeleteMailRequest request,
        io.grpc.stub.StreamObserver<tigro.Tigro.DeleteMailReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_MAIL, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static final class TigroServiceBlockingStub extends io.grpc.stub.AbstractStub<TigroServiceBlockingStub> {
    private TigroServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TigroServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TigroServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public tigro.Tigro.HelloReply hello(tigro.Tigro.HelloRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_HELLO, getCallOptions(), request);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public tigro.Tigro.CreatePOBoxReply createPOBox(tigro.Tigro.CreatePOBoxRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREATE_POBOX, getCallOptions(), request);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.DropMailReply dropMail(tigro.Tigro.DropMailRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DROP_MAIL, getCallOptions(), request);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.GetMailReply getMail(tigro.Tigro.GetMailRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_MAIL, getCallOptions(), request);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public tigro.Tigro.DeleteMailReply deleteMail(tigro.Tigro.DeleteMailRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_MAIL, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * No KeyGen protocol since server does not participate in KeyGen.
   * No explicit Init protocol since the server does it on its own at initial startup.
   * </pre>
   */
  public static final class TigroServiceFutureStub extends io.grpc.stub.AbstractStub<TigroServiceFutureStub> {
    private TigroServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TigroServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TigroServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TigroServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * For testing purposes.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.HelloReply> hello(
        tigro.Tigro.HelloRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request);
    }

    /**
     * <pre>
     * CreatePOBox creates a new PiBase-based GPOB, associates it with a new poid, and
     * sends the poid to the party in response.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.CreatePOBoxReply> createPOBox(
        tigro.Tigro.CreatePOBoxRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREATE_POBOX, getCallOptions()), request);
    }

    /**
     * <pre>
     * DropMail adds the new label-mail pair to the specified GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.DropMailReply> dropMail(
        tigro.Tigro.DropMailRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DROP_MAIL, getCallOptions()), request);
    }

    /**
     * <pre>
     * GetMail retrieves the mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.GetMailReply> getMail(
        tigro.Tigro.GetMailRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_MAIL, getCallOptions()), request);
    }

    /**
     * <pre>
     * DeleteMail deletes any mail associated with the given label from the specified
     * GPOB (by poid).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<tigro.Tigro.DeleteMailReply> deleteMail(
        tigro.Tigro.DeleteMailRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_MAIL, getCallOptions()), request);
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

  private static final class TigroServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return tigro.Tigro.getDescriptor();
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
              .setSchemaDescriptor(new TigroServiceDescriptorSupplier())
              .addMethod(METHOD_HELLO)
              .addMethod(METHOD_CREATE_POBOX)
              .addMethod(METHOD_DROP_MAIL)
              .addMethod(METHOD_GET_MAIL)
              .addMethod(METHOD_DELETE_MAIL)
              .build();
        }
      }
    }
    return result;
  }
}
