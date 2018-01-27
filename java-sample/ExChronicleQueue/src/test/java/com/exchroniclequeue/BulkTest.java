package com.exchroniclequeue;


import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    // 10억건.
    final static long MAX_PUT_COUNT = 1_000_000_000L;
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    final String path = "bulk";

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(path));
    }

    @Test
    public void _1_테스트_BULK_PUT() {
        String padding = StringUtils.leftPad("0", 1024);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptAppender appender = queue.acquireAppender();
            for (long i = 0; i < MAX_PUT_COUNT; i++) {
                appender.writeText(String.valueOf(i)+padding);
                if (i % 10_000_000 == 0) {
                    logger.info("PUT {}", i);
                }
            }
        }
        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
