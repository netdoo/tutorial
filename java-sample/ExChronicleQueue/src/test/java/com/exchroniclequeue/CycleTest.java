package com.exchroniclequeue;


import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
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

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CycleTest {

    final static Logger logger = LoggerFactory.getLogger(CycleTest.class);
    final String path = "cycle-queue";

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(path));
    }

    @Test
    public void _1_테스트_() {

        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptAppender appender = queue.acquireAppender();
            appender.writeText("1");
            appender.writeText("2");
            appender.writeText("3");
            appender.writeText("4");
            int a = queue.cycle();
            appender.writeText("a");
            appender.writeText("b");
            appender.writeText("c");
            appender.writeText("d");
        }
    }

    @Test
    public void _2_테스트_SIZE() {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptTailer tailer = queue.createTailer();

            while (tailer.n)

            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
        }
    }
}

/*
https://github.com/OpenHFT/Chronicle-Queue

How to change the time that Chronicle Queue rolls
The time Chronicle Queue rolls, is based on the UTC time, it uses System.currentTimeMillis().

When using daily-rolling, Chronicle Queue will roll at midnight UTC. If you wish to change the time it rolls, you have to change Chronicle Queue’s epoch() time. This time is a milliseconds offset, in other words, if you set the epoch to be epoch(1) then chronicle will roll at 1 millisecond past midnight.

Path path = Files.createTempDirectory("rollCycleTest");
SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).epoch(0).build();
We do not recommend that you change the epoch() on an existing system, which already has .cq4 files created, using a different epoch() setting.

If you were to set :

.epoch(System.currentTimeMillis()
This would make the current time the roll time, and the cycle numbers would start from zero.

How to find the current cq4 Chronicle Queue is writing to
WireStore wireStore = queue.storeForCycle(queue.cycle(), 0, false);
System.out.println(wireStore.file().getAbsolutePath());
Do we have to use Wire, can we use Bytes?
You can access the bytes in wire as follows:
 */
