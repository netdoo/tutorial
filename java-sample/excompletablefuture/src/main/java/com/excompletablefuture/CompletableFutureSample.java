package com.excompletablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureSample {

    final static Logger logger = LoggerFactory.getLogger(CompletableFutureSample.class);

    public static String foo() throws Exception {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        Thread.sleep(1000);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }

    public static String bar() throws Exception {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        Thread.sleep(1500);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }

    public static String zoo() throws Exception {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        Thread.sleep(700);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    public static void main(String[] args) throws Exception {

        logger.info("Thread {} 동기방식 CompletableFuture 예제 시작", Thread.currentThread().getId());

        CompletableFuture<String> fooFuture = CompletableFuture.completedFuture(foo());
        CompletableFuture<String> barFuture = CompletableFuture.completedFuture(bar());
        CompletableFuture<String> zooFuture = CompletableFuture.completedFuture(zoo());

        logger.info("Thread {} 동기방식 CompletableFuture 작업 대기", Thread.currentThread().getId());
        CompletableFuture.allOf(fooFuture, barFuture, zooFuture).join();
        logger.info("Thread {} 동기방식 CompletableFuture 작업 종료", Thread.currentThread().getId());

        logger.info(fooFuture.get());
        logger.info(barFuture.get());
        logger.info(zooFuture.get());

        logger.info("Thread {} 동기방식 CompletableFuture 예제 종료", Thread.currentThread().getId());
    }
}
