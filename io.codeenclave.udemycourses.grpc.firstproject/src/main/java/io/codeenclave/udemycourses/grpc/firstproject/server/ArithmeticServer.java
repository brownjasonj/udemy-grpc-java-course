package io.codeenclave.udemycourses.grpc.firstproject.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ArithmeticServer {
    private static final Logger logger = LoggerFactory.getLogger(ArithmeticServer.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("Hello gRPC");

        Server server = ServerBuilder.forPort(50052)
                .addService(new IntArithmeticServiceImpl())
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
