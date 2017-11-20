package com.excompletablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureThenApplySample {

    final static Logger logger = LoggerFactory.getLogger(CompletableFutureThenApplySample.class);

    static void sleepSecs(int secs) {
        try {
            Thread.sleep(secs * 1_000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String foo() {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        sleepSecs(5);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }

    static String bar() {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        sleepSecs(2);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }

    static String zoo() {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        sleepSecs(3);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    static void case1(ExecutorService executorService) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
            return foo();
        }, executorService).thenApply(result -> {
            return result + bar();
        }).thenApply(result -> {
            return result + zoo();
        }).thenAccept(finalResult -> {
            logger.info("finalResult {}", finalResult);
        });

        completableFuture.join();
    }

    static void case2(ExecutorService executorService) throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return foo();
        }, executorService).thenApply(result -> {
            return result + bar();
        }).thenApply(result -> {
            return result + zoo();
        });

        completableFuture.join();
        String finalResult = completableFuture.get();
        logger.info("finalResult {}", finalResult);
    }

    static void case3(ExecutorService executorService) throws Exception {
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            return foo();
        }, executorService).thenApply(result -> {
            return result + bar();
        }).thenApply(result -> {
            return result + zoo();
        });

        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            return foo();
        }, executorService).thenApply(result -> {
            return result + bar();
        }).thenApply(result -> {
            return result + zoo();
        });

        CompletableFuture.allOf(completableFuture1, completableFuture2).join();
    }

    public static void main(String[] args) throws Exception {

        logger.info("Thread {} 동기방식 CompletableFuture thenApply 예제 시작", Thread.currentThread().getId());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        case1(executorService);
        case2(executorService);
        case3(executorService);
        logger.info("Thread {} 동기방식 CompletableFuture thenApply 예제 종료", Thread.currentThread().getId());
    }
}
