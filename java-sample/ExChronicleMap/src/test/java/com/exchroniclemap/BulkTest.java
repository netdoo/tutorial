package com.exchroniclemap;


import net.openhft.chronicle.map.ChronicleMap;
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

    final static long MAX_PUT_COUNT = 100_000_000L;
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    File file = new File("./bulkMap.dat");

    @Test
    public void _0_테스트_준비() {
        if (file.exists())
            file.delete();
    }

    @Test
    public void _1_테스트_BULK_PUT() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .entries(2_000_000)
                .averageKeySize(100)
                .averageValueSize(4096)
                .maxBloatFactor(1000)
                .createPersistedTo(file)) {

            for (long i = 0; i < MAX_PUT_COUNT; i++) {

                map.put("123456789"+i, "012345678901234567890123456789");

                if (i > 0 && i % 100_000 == 0) {
                    logger.info("put {}", i);
                }
            }

            assertEquals(MAX_PUT_COUNT, map.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
