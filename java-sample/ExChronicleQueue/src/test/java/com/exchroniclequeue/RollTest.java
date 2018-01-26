package com.exchroniclequeue;


import net.openhft.chronicle.queue.*;
import net.openhft.chronicle.queue.impl.StoreFileListener;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RollTest {

    final static Logger logger = LoggerFactory.getLogger(RollTest.class);
    final String path = "roll";

    @Test
    public void _Roll_테스트() throws Exception {
        FileUtils.deleteDirectory(new File(path));
        Map<Integer, File> oldFiles = new HashMap<>();
        int lastCycle = 0;

        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                long deadLine = System.currentTimeMillis() + 150_000;

                while (System.currentTimeMillis() < deadLine) {
                    try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
                        ExcerptTailer tailer = queue.createTailer();
                        String text = null;

                        while ((text = tailer.readText()) != null) {
                            logger.info("{}", text);
                        }
                    }
                }
            }
        });



        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.MINUTELY).storeFileListener(new StoreFileListener() {
            @Override
            public void onReleased(int i, File file) {
                if (file != null) {
                    oldFiles.put(i, file);
                    logger.info("release file {} cycle {}", file.getName(), i);
                }
            }
        }).build()) {
            ExcerptAppender appender = queue.acquireAppender();

            for (int i = 0; i < 15; i++) {
                appender.writeText("first" + i);
                logger.info("write first {} lastCycle {}", i, queue.lastCycle());
                lastCycle = queue.lastCycle();

                if (i == 0) {
                    reader.start();
                }
                Thread.sleep(5_000);
            }
        }

        for (Map.Entry<Integer, File> entry : oldFiles.entrySet()) {
            if (entry.getKey() != lastCycle) {
                File qFile = entry.getValue();
                logger.info("delete file {} result {}", qFile.getName(), qFile.delete());
            }
        }


        reader.join();
    }



    /*
    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(path));
    }

    @Test
    public void _1_큐에쓰기() throws Exception {

        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.TEST_SECONDLY).storeFileListener(new StoreFileListener() {
            @Override
            public void onReleased(int i, File file) {
                if (file != null) {
                    logger.info("release queue file {}", file.getAbsolutePath());
                    file.delete();
                }
            }
        }).build()) {

            ExcerptAppender appender = queue.acquireAppender();

            for (int i = 0; i < 1_000; i++) {
                appender.writeText(String.valueOf(i));
                Thread.sleep(1);
            }

            ExcerptTailer tailer = queue.createTailer();
            String text = null;

            while ((text = tailer.readText()) != null) {
                logger.info("{}", text);
            }
        }
    }


    @Test
    public void _2_큐에서_읽기() throws Exception {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.TEST_SECONDLY).build()) {
            ExcerptTailer tailer = queue.createTailer();
            String text = null;

            while ((text = tailer.readText()) != null) {
                logger.info("{}", text);
            }
        }
    }

    /*
    @Test
    public void _3_큐에쓰기() throws Exception {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.TEST_SECONDLY).build()) {
            ExcerptAppender appender = queue.acquireAppender();
            appender.writeText("10");
            appender.writeText("20");
            appender.writeText("30");
            appender.writeText("40");
        }
    }

    @Test
    public void _4_큐에서_읽기() throws Exception {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).rollCycle(RollCycles.TEST_SECONDLY).build()) {
            ExcerptTailer tailer = queue.createTailer();
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
        }
    }
    */
}

/*
https://github.com/OpenHFT/Chronicle-Queue

How to change the time that Chronicle Queue rolls
The time Chronicle Queue rolls, is based on the UTC time, it uses System.currentTimeMillis().

When using daily-rolling, Chronicle Queue will roll at midnight UTC.
If you wish to change the time it rolls,
you have to change Chronicle Queue’s epoch() time.

This time is a milliseconds offset, in other words,
if you set the epoch to be epoch(1) then chronicle will roll at 1 millisecond past midnight.

Path path = Files.createTempDirectory("rollCycleTest");
SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).epoch(0).build();
We do not recommend that you change the epoch() on an existing system,
which already has .cq4 files created, using a different epoch() setting.

If you were to set :

.epoch(System.currentTimeMillis()

This would make the current time the roll time,
and the cycle numbers would start from zero.

How to find the current cq4 Chronicle Queue is writing to
WireStore wireStore = queue.storeForCycle(queue.cycle(), 0, false);
System.out.println(wireStore.file().getAbsolutePath());
Do we have to use Wire, can we use Bytes?
You can access the bytes in wire as follows:
 */
