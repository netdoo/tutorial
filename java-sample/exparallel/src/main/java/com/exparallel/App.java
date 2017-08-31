package com.exparallel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    static void sleep(int secs) {
        try {
            TimeUnit.SECONDS.sleep(secs);
        } catch (Exception e) {}
    }

    static void parallel() {
        IntStream.range(0, 10).parallel().forEach(index -> {
            logger.info("Starting {}, index {}", Thread.currentThread().getName(), index);
            sleep(5);
        });
    }

    static void parallelWithCustomPool() throws Exception {
        ForkJoinPool myPool = new ForkJoinPool(10);
        ForkJoinTask forkJoinTask = myPool.submit(new Runnable() {
            @Override
            public void run() {
                IntStream.range(0, 10).parallel().forEach(index -> {
                    logger.info("start {}, index {}", Thread.currentThread().getName(), index);
                    sleep(5);
                    logger.info("done {}, index {}", Thread.currentThread().getName(), index);
                });
            }
        });

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        myPool.shutdown();
        myPool.awaitTermination(10, TimeUnit.MINUTES);
        logger.info("done");
    }

    public static void main( String[] args) throws Exception {
        parallel();
        parallelWithCustomPool();
    }
}
