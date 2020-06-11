package io.codeenclave.udemycourses.grpc.firstproject.primenumbers.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PrimeNumberDecompositionServer {
    private static final Logger logger = LoggerFactory.getLogger(PrimeNumberDecompositionServer.class);


    public static void main(String[] args) throws IOException, InterruptedException {

        logger.info("Hello gRPC");

        Server server = ServerBuilder.forPort(50053)
                .addService(new PrimeNumberDecompositionServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Received Shutdown Request");
            server.shutdown();
            logger.info("Successfully stopped the server");
        }));

        server.awaitTermination();
    }
}
