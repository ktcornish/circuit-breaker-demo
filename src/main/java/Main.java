import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .withBackoff(1L, 10L, ChronoUnit.SECONDS)
                .onRetriesExceeded(eae -> logger.debug("Retries exceeded {}", eae))
                .onFailedAttempt(eae -> logger.debug("Failed attempt {}", eae))
                .onRetry(eae -> logger.debug("Retry {}", eae))
                .withMaxRetries(10);

        CircuitBreaker<Object> circuitBreaker = new CircuitBreaker<>()
                .withFailureThreshold(3)
                .withSuccessThreshold(3)
                .withDelay(Duration.ofSeconds(5))
                .onClose(() -> logger.debug("Closed"))
                .onOpen(() -> logger.debug("Open"))
                .onHalfOpen(() -> logger.debug("Half Open"));

        Failsafe
                .with(retryPolicy, circuitBreaker)
                .run(Main::startRequests);

    }

    private static void startRequests() throws IOException {
        while (true) {
            newRequest();
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}

private static void newRequest() throws IOException {
    URL helloServer = new URL("http://localhost:5050/");

    try (InputStreamReader isr = new InputStreamReader(helloServer.openConnection().getInputStream());
         BufferedReader br = new BufferedReader(isr)) {

        String inputLine;
        while ((inputLine = br.readLine()) != null)
            logger.debug(inputLine);
        }
    }

}

