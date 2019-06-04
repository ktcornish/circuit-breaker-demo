import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.event.ExecutionAttemptedEvent;
import net.jodah.failsafe.function.CheckedConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .handle(ConnectException.class)
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);

        CircuitBreaker<Object> breaker = new CircuitBreaker<>()
                .handle(ConnectException.class)
                .withFailureThreshold(3)
                .withSuccessThreshold(3)
                .withDelay(Duration.ofSeconds(10))
                .onClose(() -> System.out.println("The circuit breaker was closed"))
                .onOpen(() -> System.out.println("The circuit breaker was opened"))
                .onHalfOpen(() -> System.out.println("The circuit breaker was half opened"));

        Fallback<Object> fallback = Fallback.of((CheckedConsumer<ExecutionAttemptedEvent<?>>) failure -> { throw new Exception(); });

        Request request = new Request();

        Failsafe.with(fallback, retryPolicy, breaker).run(request);

    }



//    private static void startRequests() {
//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    newRequest();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, 2000);
//    }

//private static void newRequest() throws Exception {
//    URL helloServer = new URL("http://localhost:5050/jedd");
//    URLConnection helloConnection = helloServer.openConnection();
//    BufferedReader in = new BufferedReader(
//            new InputStreamReader(
//                    helloConnection.getInputStream()));
//    String inputLine;
//
//    while ((inputLine = in.readLine()) != null)
//        System.out.println(inputLine);
//    in.close();
//    }
}

