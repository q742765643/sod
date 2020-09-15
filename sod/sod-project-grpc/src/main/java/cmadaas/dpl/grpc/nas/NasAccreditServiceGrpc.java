package cmadaas.dpl.grpc.nas;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * 定义通用的 Grpc 服务
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.1)",
    comments = "Source: nas.proto")
public final class NasAccreditServiceGrpc {

  private NasAccreditServiceGrpc() {}

  public static final String SERVICE_NAME = "NasAccreditService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<cmadaas.dpl.grpc.nas.NasAccredit.NasRequest,
      cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> getHandleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "handle",
      requestType = cmadaas.dpl.grpc.nas.NasAccredit.NasRequest.class,
      responseType = cmadaas.dpl.grpc.nas.NasAccredit.NasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<cmadaas.dpl.grpc.nas.NasAccredit.NasRequest,
      cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> getHandleMethod() {
    io.grpc.MethodDescriptor<cmadaas.dpl.grpc.nas.NasAccredit.NasRequest, cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> getHandleMethod;
    if ((getHandleMethod = NasAccreditServiceGrpc.getHandleMethod) == null) {
      synchronized (NasAccreditServiceGrpc.class) {
        if ((getHandleMethod = NasAccreditServiceGrpc.getHandleMethod) == null) {
          NasAccreditServiceGrpc.getHandleMethod = getHandleMethod =
              io.grpc.MethodDescriptor.<cmadaas.dpl.grpc.nas.NasAccredit.NasRequest, cmadaas.dpl.grpc.nas.NasAccredit.NasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "handle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cmadaas.dpl.grpc.nas.NasAccredit.NasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  cmadaas.dpl.grpc.nas.NasAccredit.NasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NasAccreditServiceMethodDescriptorSupplier("handle"))
              .build();
        }
      }
    }
    return getHandleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NasAccreditServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceStub>() {
        @java.lang.Override
        public NasAccreditServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NasAccreditServiceStub(channel, callOptions);
        }
      };
    return NasAccreditServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NasAccreditServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceBlockingStub>() {
        @java.lang.Override
        public NasAccreditServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NasAccreditServiceBlockingStub(channel, callOptions);
        }
      };
    return NasAccreditServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NasAccreditServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NasAccreditServiceFutureStub>() {
        @java.lang.Override
        public NasAccreditServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NasAccreditServiceFutureStub(channel, callOptions);
        }
      };
    return NasAccreditServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 定义通用的 Grpc 服务
   * </pre>
   */
  public static abstract class NasAccreditServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 处理请求
     * </pre>
     */
    public void handle(cmadaas.dpl.grpc.nas.NasAccredit.NasRequest request,
        io.grpc.stub.StreamObserver<cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getHandleMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHandleMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                cmadaas.dpl.grpc.nas.NasAccredit.NasRequest,
                cmadaas.dpl.grpc.nas.NasAccredit.NasResponse>(
                  this, METHODID_HANDLE)))
          .build();
    }
  }

  /**
   * <pre>
   * 定义通用的 Grpc 服务
   * </pre>
   */
  public static final class NasAccreditServiceStub extends io.grpc.stub.AbstractAsyncStub<NasAccreditServiceStub> {
    private NasAccreditServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NasAccreditServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NasAccreditServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 处理请求
     * </pre>
     */
    public void handle(cmadaas.dpl.grpc.nas.NasAccredit.NasRequest request,
        io.grpc.stub.StreamObserver<cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getHandleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * 定义通用的 Grpc 服务
   * </pre>
   */
  public static final class NasAccreditServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<NasAccreditServiceBlockingStub> {
    private NasAccreditServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NasAccreditServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NasAccreditServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 处理请求
     * </pre>
     */
    public cmadaas.dpl.grpc.nas.NasAccredit.NasResponse handle(cmadaas.dpl.grpc.nas.NasAccredit.NasRequest request) {
      return blockingUnaryCall(
          getChannel(), getHandleMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 定义通用的 Grpc 服务
   * </pre>
   */
  public static final class NasAccreditServiceFutureStub extends io.grpc.stub.AbstractFutureStub<NasAccreditServiceFutureStub> {
    private NasAccreditServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NasAccreditServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NasAccreditServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 处理请求
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cmadaas.dpl.grpc.nas.NasAccredit.NasResponse> handle(
        cmadaas.dpl.grpc.nas.NasAccredit.NasRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getHandleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HANDLE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NasAccreditServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NasAccreditServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HANDLE:
          serviceImpl.handle((cmadaas.dpl.grpc.nas.NasAccredit.NasRequest) request,
              (io.grpc.stub.StreamObserver<cmadaas.dpl.grpc.nas.NasAccredit.NasResponse>) responseObserver);
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

  private static abstract class NasAccreditServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NasAccreditServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cmadaas.dpl.grpc.nas.NasAccredit.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NasAccreditService");
    }
  }

  private static final class NasAccreditServiceFileDescriptorSupplier
      extends NasAccreditServiceBaseDescriptorSupplier {
    NasAccreditServiceFileDescriptorSupplier() {}
  }

  private static final class NasAccreditServiceMethodDescriptorSupplier
      extends NasAccreditServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NasAccreditServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (NasAccreditServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NasAccreditServiceFileDescriptorSupplier())
              .addMethod(getHandleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
