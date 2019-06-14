package com.themis.circuitbreakerdemo;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

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

    private static void newRequest() throws IOException {
        URL helloServer = new URL("http://localhost:5050/");

        InputStreamReader isr = new InputStreamReader(helloServer.openConnection().getInputStream());
        BufferedReader br = new BufferedReader(isr);

        Stream<String> inputLine = br.lines();
        inputLine.forEach(s -> logger.debug(s));

    }

}

