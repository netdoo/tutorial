package com.excollection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * VM Options : -Xmx320m
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AtomicRefTest {
    final static Logger logger = LoggerFactory.getLogger(AtomicRefTest.class);
    final AtomicReference<List<String>> atomicReference = new AtomicReference<>(Collections.emptyList());
    final int maxReaders = 3;
    final long testTime = 10 * 60 * 1000;        // 2 mins
    final long deadLine = System.currentTimeMillis() + testTime;
    CountDownLatch countDownLatch = new CountDownLatch(maxReaders+1);

    void unsafeSleep(long millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (Exception e) {}
    }

    List<String> createDummyList(long from, long count) {
        List<String> list = new ArrayList<>();

        for (long i = from; i < from + count; i++) {
            list.add(String.valueOf(i));
        }

        return list;
    }

    long usedMemory() {
        int mb = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / mb;
    }

    @Test
    public void _0_테스트_준비() throws Exception {
    }

    @Test
    public void _1_Thread_테스트() throws Exception {

        Thread writer = new Thread(() -> {
            long counter = 0;

            while (System.currentTimeMillis() < deadLine) {
                List<String> mutableList = createDummyList(counter++, 1000);
                atomicReference.set(Collections.unmodifiableList(new ArrayList<>(mutableList)));
                unsafeSleep(20);

                if (counter % 100 == 0) {
                    logger.info("{} used memory {} (mb) ", counter, usedMemory());
                }
            }

            countDownLatch.countDown();
        });

        writer.start();
        unsafeSleep(1_000);

        ThreadGroup threadGroup = new ThreadGroup("main");

        IntStream.range(0, maxReaders).forEach(threadNo -> {
            Thread reader = new Thread(threadGroup, () -> {
                while(System.currentTimeMillis() < deadLine) {
                    final List<String> immutableList = atomicReference.get();
                    if (immutableList.size() < 1) {
                        logger.error("abnormal ref");
                        System.exit(1);
                    }
                }

                countDownLatch.countDown();
            });

            reader.start();
        });

        logger.info("{}", ManagementFactory.getRuntimeMXBean().getName());
        countDownLatch.await(10, TimeUnit.MINUTES);
    }
}
