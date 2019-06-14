package com.themis.circuitbreakerdemo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerTaskTests {

    private static final Logger logger = LoggerFactory.getLogger(TimerTaskTests.class);

    @Test
    public void test() {

        logger.debug("Making runnable...");

        Runnable runnable = () -> {
            for (int i=0; i<20; i++) {
                logger.debug("I am running! {}", i);
            }
        };

        logger.debug("Making thread...");

        Thread thread = new Thread(runnable);
        thread.setName("Jedd's Thread");
        logger.debug("Starting...");
        thread.start();

        logger.debug("Finished...");

        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
