package com.themis.circuitbreakerdemo;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import net.jodah.failsafe.function.CheckedRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public final class RequestCheckedRunnable implements CheckedRunnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestCheckedRunnable.class);
    private final AtomicInteger atomicInteger200s = new AtomicInteger();
    private final AtomicInteger atomicInteger300s = new AtomicInteger();
    private final AtomicInteger atomicInteger400s = new AtomicInteger();
    private final AtomicInteger atomicInteger500s = new AtomicInteger();


    private final HttpRequest request;

    RequestCheckedRunnable(String url) throws IOException {

        URL helloServer = new URL(url);
        GenericUrl genericUrl = new GenericUrl(helloServer);

        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        this.request = requestFactory.buildGetRequest(genericUrl);
    }

    @Override
    public void run() throws Exception {
        HttpResponse rawResponse = request.execute();
        logger.debug("Response {}", rawResponse.parseAsString());

        int statusCode = rawResponse.getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            atomicInteger200s.incrementAndGet();
        } else if (statusCode >= 300 && statusCode < 400) {
            atomicInteger300s.incrementAndGet();
        } else if (statusCode >= 400 && statusCode < 500) {
            atomicInteger400s.incrementAndGet();
        } else if (statusCode >= 500) {
            atomicInteger500s.incrementAndGet();
        }
    }

    public int get200s() {
        return atomicInteger200s.get();
    }

    public int get300s() {
        return atomicInteger300s.get();
    }

    public int get400s() {
        return atomicInteger400s.get();
    }

    public int get500s() {
        return atomicInteger500s.get();
    }
}
