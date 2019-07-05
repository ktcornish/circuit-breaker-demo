package com.themis.circuitbreakerdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class App implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        try {
            String url = "http://localhost:5050/";
            RequestCheckedRunnable checkedRunnable = new RequestCheckedRunnable(url);
            RequestTimerTask timerTask = new RequestTimerTask(checkedRunnable);
            App app = new App(timerTask);
            app.run();
        } catch (IOException e) {
            logger.error("Horrible code", e);
        }
    }

    private final TimerTask task;

    App(TimerTask task) {
        this.task = task;
    }

    @Override
    public void run() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);

    }
}
