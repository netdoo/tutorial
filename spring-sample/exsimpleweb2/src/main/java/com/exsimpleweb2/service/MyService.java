package com.exsimpleweb2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MyService {
    final static Logger logger = LoggerFactory.getLogger(MyService.class);

    String foo() throws Exception {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        Thread.sleep(1000);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }

    String bar() throws Exception {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        Thread.sleep(1500);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }

    String zoo() throws Exception {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        Thread.sleep(700);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    @Async
    public CompletableFuture<String> getFoo() throws Exception {
        return CompletableFuture.completedFuture(foo());
    }

    @Async
    public CompletableFuture<String> getBar() throws Exception {
        return CompletableFuture.completedFuture(bar());
    }

    @Async
    public CompletableFuture<String> getZoo() throws Exception {
        return CompletableFuture.completedFuture(zoo());
    }
}
