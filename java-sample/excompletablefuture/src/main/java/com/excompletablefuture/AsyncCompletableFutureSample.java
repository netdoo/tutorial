package com.excompletablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncCompletableFutureSample {

    final static Logger logger = LoggerFactory.getLogger(AsyncCompletableFutureSample.class);

    static class TaskSupplier implements Supplier<String> {

        String result;
        long sleepTerm;

        public TaskSupplier(String result, long sleepTerm) {
            this.result = result;
            this.sleepTerm = sleepTerm;
        }

        @Override
        public String get() {
            logger.info("Thread {} Start {}", Thread.currentThread().getId(), result);
            try {
                Thread.sleep(sleepTerm);
            } catch (Exception e) { }
            logger.info("Thread {} Finish {}", Thread.currentThread().getId(), result);
            return result;
        }
    }

    public static void sample1() throws Exception {
        logger.info("Thread {} 비동기방식 CompletableFuture 예제 시작", Thread.currentThread().getId());
        CompletableFuture<String> fooFuture = CompletableFuture.supplyAsync(new TaskSupplier("foo", 1000));
        CompletableFuture<String> barFuture = CompletableFuture.supplyAsync(new TaskSupplier("bar", 1500));
        CompletableFuture<String> zooFuture = CompletableFuture.supplyAsync(new TaskSupplier("zoo", 700));

        logger.info("Thread {} 비동기방식 CompletableFuture 작업 대기", Thread.currentThread().getId());
        CompletableFuture.allOf(fooFuture, barFuture, zooFuture).join();
        logger.info("Thread {} 비동기방식 CompletableFuture 작업 종료", Thread.currentThread().getId());

        logger.info(fooFuture.get());
        logger.info(barFuture.get());
        logger.info(zooFuture.get());
        logger.info("Thread {} 비동기방식 CompletableFuture 예제 종료", Thread.currentThread().getId());
    }

    public static void sample2() throws Exception {
        logger.info("Thread {} 비동기방식 CompletableFuture 예제 시작", Thread.currentThread().getId());
        CompletableFuture<String> fooFuture = CompletableFuture.supplyAsync(new TaskSupplier("foo", 1000));
        CompletableFuture<String> barFuture = CompletableFuture.supplyAsync(new TaskSupplier("bar", 1500));
        CompletableFuture<String> zooFuture = CompletableFuture.supplyAsync(new TaskSupplier("zoo", 700));

        List<CompletableFuture<String>> taskList = new ArrayList<>();

        taskList.add(fooFuture);
        taskList.add(barFuture);
        taskList.add(zooFuture);

        logger.info("Thread {} 비동기방식 CompletableFuture 작업 대기", Thread.currentThread().getId());

        CompletableFuture<List<String>> completableFutureList = CompletableFuture.allOf(taskList.toArray(new CompletableFuture[taskList.size()]))
                .thenApply(v -> taskList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        logger.info("Thread {} 비동기방식 CompletableFuture 작업 종료", Thread.currentThread().getId());

        List<String> resultList = completableFutureList.get();
        resultList.forEach(result -> logger.info("{}", result));

        logger.info("Thread {} 비동기방식 CompletableFuture 예제 종료", Thread.currentThread().getId());
    }

    public static void main(String[] args) throws Exception {
        sample2();
    }
}
