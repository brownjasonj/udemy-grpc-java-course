package io.codeenclave.udemycourses.grpc.firstproject.greeting.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingStreamingClient {
    private static final Logger logger = LoggerFactory.getLogger(GreetingStreamingClient.class);

    public static void main(String[] args) {
        logger.info("Hello I'm a gRPC client");

        logger.info("Creating channel");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // create a synchronous client
        logger.info("Creating client stub");

        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

        // create an asynchronous client
        // DummyServiceGrpc.DummyServiceFutureStub aSyncClient = DummyServiceGrpc.newFutureStub(channel);

        // created a greet service client (blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // create a protocol buffer Greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Jason")
                .setLastName("Brown")
                .build();

        // create a protocol buffer GreetManyTimesRequest message
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // call the RPC streaming interface
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    logger.info(greetManyTimesResponse.getResult());
                });

        logger.info("Shutting down channel");
        channel.shutdown();
    }
}
