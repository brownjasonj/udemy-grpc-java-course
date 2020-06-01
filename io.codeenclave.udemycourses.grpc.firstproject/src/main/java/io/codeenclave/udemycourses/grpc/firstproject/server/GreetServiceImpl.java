package io.codeenclave.udemycourses.grpc.firstproject.server;

import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetRequest;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetResponse;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.GreetServiceGrpc;
import io.codeenclave.udemycourses.grpc.firstproject.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GreetServiceImpl.class);

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        // extract the fields we need
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();

        // create the response
        String result = "Hello " + firstName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        // send the response
        responseObserver.onNext(response);

        // complete the RPC call
        responseObserver.onCompleted();
    }
}
