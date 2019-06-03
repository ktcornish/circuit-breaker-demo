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
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        CircuitBreaker breaker = new CircuitBreaker()
                .withFailureThreshold(3, 10)
                .withSuccessThreshold(5);



        Failsafe.with(breaker).run(() -> startRequests());

    }

    private static void startRequests() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    newRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);
    }

private static void newRequest() throws IOException {
    URL helloServer = new URL("http://localhost:5050/jedd");
    URLConnection helloConnection = helloServer.openConnection();
    BufferedReader in = new BufferedReader(
            new InputStreamReader(
                    helloConnection.getInputStream()));
    String inputLine;

    while ((inputLine = in.readLine()) != null)
        System.out.println(inputLine);
    in.close();
    }
}

