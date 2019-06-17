package com.themis.circuitbreakerdemo;

import net.jodah.failsafe.function.CheckedRunnable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestTimerTaskTests {

    @Test
    public void test() {
        Config config = new Config();
        Counter runnableCounter = new Counter();
        RequestTimerTask requestTimerTask = new RequestTimerTask(config, runnableCounter);
        requestTimerTask.run();
        assertEquals(1, runnableCounter.count);
    }

    class Counter implements CheckedRunnable {
        int count = 0;

        @Override
        public void run() throws Exception {
            count++;
        }
    }
}
