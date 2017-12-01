package com.exberkeleydb;

import com.exberkeleydb.domain.Box;
import com.sleepycat.je.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    final static String dbDir = "./db";
    final static String dbName = "testDB";
    final static long MAX_PUT_COUNT = 100_000_000L;

    static String getNamedKey(String line) {
        String cols[] = line.split("\t");
        if (cols.length < 4) {
            logger.info("bad line {}", line);
        }
        return cols[1] + "." + cols[2];
    }

    @Test
    public void _0_테스트_준비() {

    }

    @Test
    public void _1_벌크_테스트() throws Exception {

        String line, trimLine ;
        String readPath = "C:\\temp\\naver_all.txt";
        BufferedBerkeleyDB db = new BufferedBerkeleyDB(new BerkeleyDB(dbDir, "bdb", false), 100_000);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 5; i++) {
            StopWatch subStopWatch = new StopWatch();
            subStopWatch.start();

            try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8);) {
                while ((line = in.readLine()) != null) {
                    trimLine = line.trim();

                    if (trimLine.isEmpty())
                        continue;

                    String key = getNamedKey(trimLine);
                    db.put(key+i, line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            subStopWatch.stop();
            logger.info("{} process elapsed time {} (secs), db count {}", i, subStopWatch.getTime(TimeUnit.SECONDS), db.count());
        }

        stopWatch.stop();
        logger.info("total elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
        db.close();
    }
}
