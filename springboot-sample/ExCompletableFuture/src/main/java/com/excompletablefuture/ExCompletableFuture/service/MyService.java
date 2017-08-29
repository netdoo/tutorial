package com.excompletablefuture.ExCompletableFuture.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MyService {
    final static Logger logger = LoggerFactory.getLogger(MyService.class);

    private void sleepMillSec(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (Exception e) {}
    }

    public String foo() {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        sleepMillSec(1_000);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }


    public String bar() {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        sleepMillSec(1_500);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }


    public String zoo() {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        sleepMillSec(700);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    /// CompletableFuture.completedFuture 의 인자로 동기함수를 설정하고,
    /// @Async 어노테이션을 추가하면 동기함수가 비동기 처리됨.
    @Async
    public CompletableFuture<String> getFoo() {
        return CompletableFuture.completedFuture(foo());
    }

    @Async
    public CompletableFuture<String> getBar() {
        return CompletableFuture.completedFuture(bar());
    }

    @Async
    public CompletableFuture<String> getZoo() {
        return CompletableFuture.completedFuture(zoo());
    }
}
