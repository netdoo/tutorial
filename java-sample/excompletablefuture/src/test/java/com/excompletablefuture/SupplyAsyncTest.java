package com.excompletablefuture;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplyAsyncTest {

    static ExecutorService executorService;
    static Logger logger = LoggerFactory.getLogger(SupplyAsyncTest.class);

    static void doSomething(int jobSecs) {
        try {
            TimeUnit.SECONDS.sleep(jobSecs);
        } catch (Exception e) {}
    }

    @BeforeClass
    public static void onBeforeClass() {
        executorService = Executors.newFixedThreadPool(10);
    }

    @AfterClass
    public static void onAfterClass() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void _01_thenApplyTest() throws Exception {

        CompletableFuture<String> foo = CompletableFuture.supplyAsync(() -> {
            doSomething(3);
            return "foo";
        }, executorService).thenApply(name -> {
            return name.toUpperCase();
        });

        String result;

        for (int i = 0; i < 10; i++) {
            try {
                result = foo.get(500, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                logger.info("{}", e.toString());
                continue;
            }
            logger.info("{}", result);
            break;
        }

        logger.info("{}", foo.get());
    }

    @Test
    public void _02_allOfTest() throws Exception {
        CompletableFuture<String> foo = CompletableFuture.supplyAsync(() -> {
            doSomething(3);
            return "foo";
        });

        CompletableFuture<String> bar = CompletableFuture.supplyAsync(() -> {
            doSomething(3);
            return "bar";
        });

        CompletableFuture.allOf(foo, bar).join();
        logger.info("{}", foo.get());
        logger.info("{}", bar.get());
    }
}
