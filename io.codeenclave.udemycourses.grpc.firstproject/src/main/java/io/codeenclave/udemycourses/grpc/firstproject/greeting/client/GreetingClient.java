package io.codeenclave.udemycourses.grpc.firstproject.greeting.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
    private static final Logger logger = LoggerFactory.getLogger(GreetingClient.class);

    public static void main(String[] args) {
        GreetingClient greetingClient = new GreetingClient();
        greetingClient.run();
    }


    public void run() {
        logger.info("Creating channel");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        //doUnaryCall(channel);
        //doStreamingCall(channel);
        // doClientStreamingCall(channel);
        doBiDiStreaming(channel);
        logger.info("Shutting down channel");
        channel.shutdown();
    }

    public void doUnaryCall(ManagedChannel channel) {
        // create a synchronous client
        logger.info("Creating client stub");

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

    }

    public void doStreamingCall(ManagedChannel channel) {
        // create a synchronous client
        logger.info("Creating client stub");
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
    }

    public void doClientStreamingCall(ManagedChannel channel) {
        // create
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // we get a response from the server
                logger.info("Received a response from the server");
                logger.info(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                // onCompleted will be called right after onNext
                logger.info("Server has completed sending us something");
                latch.countDown();
            }
        });

        // streaming message #1
        logger.info("Sending message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Stephane")
                        .build())
                .build());

        // streaming message #2
        logger.info("Sending message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("John")
                        .build())
                .build());

        // streaming message #3
        logger.info("Sending message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Mark")
                        .build())
                .build());

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    public void doBiDiStreaming(ManagedChannel channel) {
        // create
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
                logger.info("Response from server: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info("The server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("Stephan", "John", "Marc", "Patricia").forEach(
                name -> requestObserver.onNext(GreetEveryoneRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder()
                                .setFirstName(name)
                                .build())
                        .build())
        );

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
