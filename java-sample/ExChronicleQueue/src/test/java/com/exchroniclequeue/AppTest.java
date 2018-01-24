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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(App.class);
    final String path = "queue";
    static long lastIndex = 0;

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(path));
    }

    @Test
    public void _1_큐에쓰기() {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptAppender appender = queue.acquireAppender();
            appender.writeText("1");
            appender.writeText("2");
            appender.writeText("3");
            appender.writeText("4");
        }
    }

    @Test
    public void _2_큐에서_읽기() {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptTailer tailer = queue.createTailer();
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            lastIndex = tailer.index();
        }
    }

    @Test
    public void _3_큐에서_읽기() {
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptTailer tailer = queue.createTailer();
            tailer.moveToIndex(lastIndex);
            logger.info("{}", tailer.readText());
            logger.info("{}", tailer.readText());
            lastIndex = tailer.index();
        }
    }
}
