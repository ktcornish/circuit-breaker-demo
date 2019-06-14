package com.themis.circuitbreakerdemo;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static RetryPolicy<Object> retryPolicy;

    private static CircuitBreaker<Object> circuitBreaker;

    public static void main(String[] args) {

        retryPolicy = new RetryPolicy<>()
                .withBackoff(1L, 10L, ChronoUnit.SECONDS)
                .onRetriesExceeded(eae -> logger.debug("Retries exceeded {}", eae))
                .onFailedAttempt(eae -> logger.debug("Failed attempt {}", eae))
                .onRetry(eae -> logger.debug("Retry {}", eae))
                .withMaxRetries(10);

        circuitBreaker = new CircuitBreaker<>()
                .withFailureThreshold(3)
                .withSuccessThreshold(3)
                .withDelay(Duration.ofSeconds(5))
                .onClose(() -> logger.debug("Closed"))
                .onOpen(() -> logger.debug("Open"))
                .onHalfOpen(() -> logger.debug("Half Open"));

        TimerTask tasknew = new Main();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(tasknew, 0, 1000);
    }

    @Override
    public void run() {

        CheckedRunnable checkedRunnable = Main::newRequest;

        Failsafe.with(retryPolicy, circuitBreaker)
                .run(checkedRunnable);
    }

    public static HttpResponse newRequest() throws IOException {

        URL helloServer = new URL("http://localhost:5050/");

        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(helloServer));

        HttpResponse rawResponse = request.execute();
        String responseString = rawResponse.parseAsString();

        logger.debug(responseString);
        return rawResponse;
    }

}

