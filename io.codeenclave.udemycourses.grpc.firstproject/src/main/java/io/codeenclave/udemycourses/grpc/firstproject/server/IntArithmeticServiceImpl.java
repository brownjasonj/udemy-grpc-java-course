package io.codeenclave.udemycourses.grpc.firstproject.server;

import io.codeenclave.udemycourses.grpc.firstproject.proto.calculator.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntArithmeticServiceImpl extends IntArithmeticServiceGrpc.IntArithmeticServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(IntArithmeticServiceImpl.class);

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
}
