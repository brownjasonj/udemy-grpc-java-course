package io.codeenclave.udemycourses.grpc.firstproject.calculator.server;

import io.codeenclave.udemycourses.grpc.firstproject.proto.calculator.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntCalculatorServiceImpl extends IntArithmeticServiceGrpc.IntArithmeticServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(IntCalculatorServiceImpl.class);

    @Override
    public void arithmeticOperation(IntArithmeticRequest request, StreamObserver<IntArithmeticResponse> responseObserver) {

        IntArithmeticOperation arithmeticOperation = request.getIntOperation();
        ArithmeticBinaryOperator binaryOp = arithmeticOperation.getOp();
        int op1 = arithmeticOperation.getOp1();
        int op2 = arithmeticOperation.getOp2();

        IntArithmeticResponse response;
        int result = 0;
        switch (binaryOp) {
            case ADD:
                response = IntArithmeticResponse.newBuilder()
                    .setResult(op1 + op2)
                    .build();
                break;
            case DIVIDE:
                if (op2 != 0) {
                    response = IntArithmeticResponse.newBuilder()
                            .setResult(op1 / op2)
                            .build();
                }
                else {
                    response = IntArithmeticResponse.newBuilder()
                            .setErrorCode(ArithmeticErrorCode.NaN)
                            .build();
                }
                break;
            case MULTIPLY:
                response = IntArithmeticResponse.newBuilder()
                        .setResult(op1 * op2)
                        .build();
                break;
            case SUBTRACT:
                response = IntArithmeticResponse.newBuilder()
                        .setResult(op1 - op2)
                        .build();
                break;
            case UNKNOWN_BINARY_OPERATOR:
            default:
                response = IntArithmeticResponse.newBuilder()
                        .setErrorCode(ArithmeticErrorCode.UNKNOWN_ERROR_CODE)
                        .build();
                break;
        }

        // send the response
        responseObserver.onNext(response);

        // complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<IntAverageRequest> intCalculateAverage(StreamObserver<FloatAverageResponse> responseObserver) {
        StreamObserver<IntAverageRequest> streamObserver = new StreamObserver<IntAverageRequest>() {
            Integer sum = 0;
            Integer count = 0;

            @Override
            public void onNext(IntAverageRequest value) {
                // client sends an integer
                sum += value.getValue();
                count++;
            }

            @Override
            public void onError(Throwable t) {
                // client sends an error
            }

            @Override
            public void onCompleted() {
                //  finished sending the sequence of numbers to average
                float average = 0.0f;
                if (count != 0) {
                    average = sum / count;
                }
                responseObserver.onNext(FloatAverageResponse.newBuilder()
                        .setValue(average)
                        .build());
                responseObserver.onCompleted();
            }
        };

        return streamObserver;
    }

    @Override
    public StreamObserver<IntFindMaximumRequest> findMaximum(StreamObserver<IntFindMaximumResponse> responseObserver) {
        StreamObserver<IntFindMaximumRequest> requestObserver = new StreamObserver<IntFindMaximumRequest>() {
            Integer max = 0;
            @Override
            public void onNext(IntFindMaximumRequest value) {
                if (value.getValue() > max) {
                    max = value.getValue();
                    responseObserver.onNext(IntFindMaximumResponse.newBuilder()
                            .setValue(max)
                            .build());
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}
