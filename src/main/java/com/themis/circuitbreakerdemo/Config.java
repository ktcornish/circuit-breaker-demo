package com.themis.circuitbreakerdemo;

public class Config {

    private int retryPolicyBackoffDelay = 1;
    private int retryPolicyBackoffMaxDelay = 10;
    private int retryPolicyMaxRetries = 10;
    private int circuitBreakerFailureThreshold = 3;
    private int circuitBreakerSuccessThreshold = 3;
    private int circuitBreakerDelayDuration = 5;

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
