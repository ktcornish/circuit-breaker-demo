package com.themis.circuitbreakerdemo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadCollisionTests {

    private static final Logger logger = LoggerFactory.getLogger(ThreadCollisionTests.class);

    private volatile int counter = 0;

    @Test
    public void collide() throws InterruptedException {

        Runnable runnable1 = () -> {
            while (true) {
                counter++;
                logger.debug("Count is: {}", counter);
            }
        };

        Runnable runnable2 = () -> {
            while (true) {
                counter--;
                logger.debug("Count is: {}", counter);
            }
        };

        Runnable runnable3 = () -> {
            while (true) {
                logger.debug("Count is: {}", counter);
            }
        };

        Thread thread1 = new Thread(runnable1, "Jedd's Thread");
        Thread thread2 = new Thread(runnable2, "Katie's downer");
        Thread thread3 = new Thread(runnable3, "Stuart's observer");

        thread1.setDaemon(true);
        thread1.start();

        thread2.setDaemon(true);
        thread2.start();

        thread3.setDaemon(true);
        thread3.start();

        TimeUnit.SECONDS.sleep(5L);
    }
}
