package io.codeenclave.udemycourses.grpc.firstproject.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArithmeticClient {
    private static final Logger logger = LoggerFactory.getLogger(ArithmeticClient.class);

    public static void main(String[] args) {
        logger.info("Arithmetic gRPC client");

        logger.info("Creating channel");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        IntArithmeticServiceGrpc.IntArithmeticServiceBlockingStub intArithmeticClient = IntArithmeticServiceGrpc.newBlockingStub(channel);


        IntArithmeticOperation intArithmeticOperation = IntArithmeticOperation.newBuilder()
                .setOp(ArithmeticBinaryOperator.DIVIDE)
                .setOp1(2000)
                .setOp2(23)
                .build();


        IntArithmeticRequest intArithmeticRequest = IntArithmeticRequest.newBuilder()
                .setIntOperation(intArithmeticOperation)
                .build();

        IntArithmeticResponse response = intArithmeticClient.arithmeticOperation(intArithmeticRequest);


        switch(response.getGuardedResultCase()) {
            case ERRORCODE:
                logger.info("Int Arithmetic Error: {}", response.getErrorCode());
                break;
            case RESULT:
                logger.info("Result = {}", response.getResult());
                break;
        }

        logger.info("Shutting down channel");
        channel.shutdown();
    }
}
