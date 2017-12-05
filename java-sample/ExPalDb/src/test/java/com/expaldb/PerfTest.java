package com.expaldb;

import com.linkedin.paldb.api.PalDB;
import com.linkedin.paldb.api.StoreWriter;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerfTest {

    final static Logger logger = LoggerFactory.getLogger(PerfTest.class);

    @Test
    public void testApp() {
        String tsvLine, key;
        String id, title, pcPrice;
        String cols[];
        int lineCount = 0;

        logger.info("start");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        StoreWriter writer = PalDB.createWriter(new File("./store.paldb"));

        try (BufferedReader in = Files.newBufferedReader(Paths.get("C:\\temp\\naver_all.txt"), StandardCharsets.UTF_8);) {
            while ((tsvLine = in.readLine()) != null) {
                lineCount++;
                cols = tsvLine.split("\t");
                id = cols[0];
                title = cols[1];
                pcPrice = cols[2];
                key = title + "." + pcPrice;
                writer.put(key, tsvLine);
                cols = null;
                if (lineCount % 100_000 == 0) {
                    logger.info("process {}", lineCount);
                }
            }
        } catch (Exception e) {
            logger.error("io exception ", e);
        }

        try {
            writer.close();
        } catch (Exception e) {
            logger.error("fail to close ", e);
        }

        stopWatch.stop();
        logger.info("finish, elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
