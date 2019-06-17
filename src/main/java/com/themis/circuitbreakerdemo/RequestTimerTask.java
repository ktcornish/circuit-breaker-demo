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


    RequestTimerTask(Config config, CheckedRunnable checkedRunnable) {

        this.retryPolicy = new RetryPolicy<>()
                .withBackoff(config.getRetryPolicyBackoffDelay(), config.getRetryPolicyBackoffMaxDelay(), ChronoUnit.SECONDS)
                .onRetriesExceeded(eae -> logger.debug("Retries exceeded {}", eae))
                .onFailedAttempt(eae -> logger.debug("Failed attempt {}", eae))
                .onRetry(eae -> logger.debug("Retry {}", eae))
                .withMaxRetries(config.getRetryPolicyMaxRetries());

        this.circuitBreaker = new CircuitBreaker<>()
                .withFailureThreshold(config.getCircuitBreakerFailureThreshold())
                .withSuccessThreshold(config.getCircuitBreakerSuccessThreshold())
                .withDelay(Duration.ofSeconds(config.getCircuitBreakerDelayDuration()))
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
