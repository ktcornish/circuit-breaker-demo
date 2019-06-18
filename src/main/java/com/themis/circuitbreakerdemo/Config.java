package com.themis.circuitbreakerdemo;

public class Config {

    private static final int retryPolicyBackoffDelay = 1;
    private static final int retryPolicyBackoffMaxDelay = 10;
    private static final int retryPolicyMaxRetries = 10;
    private static final int circuitBreakerFailureThreshold = 3;
    private static final int circuitBreakerSuccessThreshold = 3;
    private static final int circuitBreakerDelayDuration = 5;

    public Config() {
    }

    public int getRetryPolicyBackoffMaxDelay() {
        return retryPolicyBackoffMaxDelay;
    }

    public int getRetryPolicyMaxRetries() {
        return retryPolicyMaxRetries;
    }

    public int getCircuitBreakerFailureThreshold() {
        return circuitBreakerFailureThreshold;
    }

    public int getCircuitBreakerSuccessThreshold() {
        return circuitBreakerSuccessThreshold;
    }

    public int getCircuitBreakerDelayDuration() {
        return circuitBreakerDelayDuration;
    }

    public int getRetryPolicyBackoffDelay() {
        return retryPolicyBackoffDelay;

    }
}
