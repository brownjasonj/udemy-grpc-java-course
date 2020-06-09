package io.codeenclave.udemycourses.grpc.firstproject.client;

import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamRequest;
import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamResponse;
import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimeNumberDecompositionClient {
    private static final Logger logger = LoggerFactory.getLogger(PrimeNumberDecompositionClient.class);


    public static void main(String[] args) {

        logger.info("Hello I'm a gRPC client");

        logger.info("Creating channel");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053)
                .usePlaintext()
                .build();

        // create a synchronous client
        logger.info("Creating client stub");


        PrimeNumberDecompositionStreamServiceGrpc.PrimeNumberDecompositionStreamServiceBlockingStub primeNumberDecompositionClient
                = PrimeNumberDecompositionStreamServiceGrpc.newBlockingStub(channel);

        // create a PrimeNumberDecompositonRequest
        PrimeNumberDecompositionStreamRequest request = PrimeNumberDecompositionStreamRequest.newBuilder()
                .setPrimeNumber(120)
                .build();

        primeNumberDecompositionClient.getPrimeNumberDecomposition(request).forEachRemaining(primeNumberDecompositionStreamResponse -> {
            logger.info("factor : " + primeNumberDecompositionStreamResponse.getResult());
        } );

        logger.info("Shutting down channel");
        channel.shutdown();
    }
}
