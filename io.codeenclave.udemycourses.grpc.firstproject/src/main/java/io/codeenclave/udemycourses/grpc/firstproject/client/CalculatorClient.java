package io.codeenclave.udemycourses.grpc.firstproject.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorClient.class);

    public static void main(String[] args) {
        logger.info("Arithmetic gRPC client");
        CalculatorClient client = new CalculatorClient();
        client.run();
    }

    public void run() {
        logger.info("Creating channel");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        // doUnaryCall(channel);
        doStreamingCall(channel);

        logger.info("Shutting down channel");
        channel.shutdown();
    }

    public void doUnaryCall(ManagedChannel channel) {
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
    }

    public void doStreamingCall(ManagedChannel channel) {
        IntArithmeticServiceGrpc.IntArithmeticServiceStub asyncClient = IntArithmeticServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Integer> numbers = new ArrayList<Integer>(
                Arrays.asList(10,20,30,40,50,60,71,80,90,91));

        StreamObserver<IntAverageRequest> requestObserver = asyncClient.intCalculateAverage(new StreamObserver<FloatAverageResponse>() {
            @Override
            public void onNext(FloatAverageResponse value) {
                logger.info("Average of " + numbers + " is " + value.getValue());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        for(int i = 0; i < numbers.size(); i++) {
            logger.info("Sending integer " + numbers.get(i));
            requestObserver.onNext(IntAverageRequest.newBuilder()
                    .setValue(numbers.get(i))
                    .build());
        };

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }


    }
}
