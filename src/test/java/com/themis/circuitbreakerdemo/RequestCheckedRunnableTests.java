package com.themis.circuitbreakerdemo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.server.RatpackServer;


import java.io.IOException;

import static org.junit.Assert.*;

public class RequestCheckedRunnableTests {

    private static final Logger logger = LoggerFactory.getLogger(RequestCheckedRunnableTests.class);

    @BeforeClass
    public static void setUp() throws Exception {
        logger.debug("Before class setup");
        RatpackServer.start(server -> server.handlers(chain -> chain
            .get(ctx -> ctx.render("Hello Jedd & Katie"))
            )
        );
    }

    @AfterClass
    public static void tearDown() {
        logger.debug("After class teardown");
    }

    @Test
    public void check200s() throws Exception {
        logger.debug("Running in 200s");
        String url = "http://localhost:5050/";

            RequestCheckedRunnable rcr = new RequestCheckedRunnable(url);
            rcr.run();

            int count200s = rcr.get200s();
            assertEquals(1, count200s);
    }
}