package com.exfuturetask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class FutureTaskApp {
    final static Logger logger = LoggerFactory.getLogger(FutureTaskApp.class);

    public static void main(String[] args) throws Exception {
        java.util.concurrent.FutureTask<Void> ft = new java.util.concurrent.FutureTask<>(() -> {
            logger.info("start future task, thread id {} ", Thread.currentThread().getId());
            Thread.sleep(300);
            logger.info("in future task, thread id {} ", Thread.currentThread().getId());

            return null;
        });

        ft.run();
        ft.get(5, TimeUnit.SECONDS);
        logger.info("done, thread id {} ", Thread.currentThread().getId());
    }
}
