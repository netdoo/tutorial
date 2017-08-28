package com.excompletablefuture.ExCompletableFuture.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MyService {
    final static Logger logger = LoggerFactory.getLogger(MyService.class);


    public String foo() throws Exception {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        Thread.sleep(1000);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }


    public String bar() throws Exception {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        Thread.sleep(1500);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }


    public String zoo() throws Exception {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        Thread.sleep(700);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    /// CompletableFuture.completedFuture 의 인자로 동기함수를 설정하고,
    /// @Async 어노테이션을 추가하면 동기함수가 비동기 처리됨.
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
