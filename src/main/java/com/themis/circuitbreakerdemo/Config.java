package com.themis.circuitbreakerdemo;

public class Config {

    public static final int RETRY_POLICY_BACKOFF_DELAY = 1;
    public static final int RETRY_POLICY_BACKOFF_MAX_DELAY = 10;
    public static final int RETRY_POLICY_MAX_RETRIES = 10;
    public static final int CIRCUIT_BREAKER_FAILURE_THRESHOLD = 3;
    public static final int CIRCUIT_BREAKER_SUCCESS_THRESHOLD = 3;
    public static final int CIRCUIT_BREAKER_DELAY_DURATION = 5;

}
