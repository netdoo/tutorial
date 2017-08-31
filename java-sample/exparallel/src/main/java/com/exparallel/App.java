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

    /// 병렬처리 스레드풀을 OS 기본값으로 사용하는 예제 (코어수 갯수만큼 병렬처리가 됨.)
    static void parallel() {

        logger.info("{} 개의 스레드에서 병렬로 처리됨.", Runtime.getRuntime().availableProcessors());

        IntStream.range(0, 5).parallel().forEach(index -> {
            logger.info("start {}, index {}", Thread.currentThread().getName(), index);
            sleep(5);
            logger.info("done {}, index {}", Thread.currentThread().getName(), index);
        });
    }

    /// 병렬처리 스레드풀을 별도로 정의하여 사용하는 예제
    static void parallelWithCustomPool() throws Exception {
        ForkJoinPool myPool = new ForkJoinPool(5);
        logger.info("{} 개의 스레드에서 병렬로 처리됨.", myPool.getParallelism());

        ForkJoinTask forkJoinTask = myPool.submit(new Runnable() {
            @Override
            public void run() {
                IntStream.range(0, 5).parallel().forEach(index -> {
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
        logger.info("===========================================================================");
        parallelWithCustomPool();
    }
}
