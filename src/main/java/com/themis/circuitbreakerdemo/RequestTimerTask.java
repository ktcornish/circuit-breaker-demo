package com.themis.circuitbreakerdemo;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;

public final class RequestTimerTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(RequestTimerTask.class);
    private final RetryPolicy<Object> retryPolicy;
    private final CircuitBreaker<Object> circuitBreaker;
    private final CheckedRunnable checkedRunnable;


    RequestTimerTask(CheckedRunnable checkedRunnable) {

        this.retryPolicy = new RetryPolicy<>()
                .withBackoff(Config.RETRY_POLICY_BACKOFF_DELAY, Config.RETRY_POLICY_BACKOFF_MAX_DELAY, ChronoUnit.SECONDS)
                .onRetriesExceeded(eae -> logger.debug("Retries exceeded {}", eae))
                .onFailedAttempt(eae -> logger.debug("Failed attempt {}", eae))
                .onRetry(eae -> logger.debug("Retry {}", eae))
                .withMaxRetries(Config.RETRY_POLICY_MAX_RETRIES);

        this.circuitBreaker = new CircuitBreaker<>()
                .withFailureThreshold(Config.CIRCUIT_BREAKER_FAILURE_THRESHOLD)
                .withSuccessThreshold(Config.CIRCUIT_BREAKER_SUCCESS_THRESHOLD)
                .withDelay(Duration.ofSeconds(Config.CIRCUIT_BREAKER_DELAY_DURATION))
                .onClose(() -> logger.debug("Closed"))
                .onOpen(() -> logger.debug("Open"))
                .onHalfOpen(() -> logger.debug("Half Open"));

        this.checkedRunnable = checkedRunnable;
    }

    @Override
    public void run() {

        Failsafe.with(retryPolicy, circuitBreaker)
                .run(checkedRunnable);
    }
}
