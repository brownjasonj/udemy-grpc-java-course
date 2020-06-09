package io.codeenclave.udemycourses.grpc.firstproject.server;

import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamRequest;
import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamResponse;
import io.codeenclave.udemycourses.grpc.firstproject.proto.primenumberdecomposition.PrimeNumberDecompositionStreamServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimeNumberDecompositionServiceImpl extends PrimeNumberDecompositionStreamServiceGrpc.PrimeNumberDecompositionStreamServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(PrimeNumberDecompositionServiceImpl.class);


    @Override
    public void getPrimeNumberDecomposition(PrimeNumberDecompositionStreamRequest request, StreamObserver<PrimeNumberDecompositionStreamResponse> responseObserver) {
        Integer primNumber = request.getPrimeNumber();

        Integer k = 2;
        Integer N = primNumber;
        try {
            while (N > 1) {
                if (N % k == 0) {   // if k evenly divides into N
                    // this is a factor
                    PrimeNumberDecompositionStreamResponse response = PrimeNumberDecompositionStreamResponse.newBuilder()
                            .setResult(k)
                            .build();

                    responseObserver.onNext(response);
                    N = N / k;    // divide N by k so that we have the rest of the number left.

                    Thread.sleep(1000L);
                } else {
                    k = k + 1;
                }
            }
        }
        catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        finally {
            responseObserver.onCompleted();
        }
    }
}
