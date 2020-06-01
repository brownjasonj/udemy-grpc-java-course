package io.codeenclave.udemycourses.grpc.firstproject.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.dummy.DummyServiceGrpc;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetRequest;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetResponse;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetServiceGrpc;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingClient {
    private static final Logger logger = LoggerFactory.getLogger(GreetingClient.class);


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

        // create a protocol buffer GreetRequest message
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // call the RPC and get back a GreetResponse message
        GreetResponse response = greetClient.greet(greetRequest);

        // do something with the client
        logger.info(response.getResult());

        logger.info("Shutting down channel");
        channel.shutdown();
    }
}
