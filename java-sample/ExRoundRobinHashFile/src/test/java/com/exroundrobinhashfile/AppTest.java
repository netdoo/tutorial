package com.exroundrobinhashfile;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest {
    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    static String getNamedKey(String line) {
        String cols[] = line.split("\t");
        if (cols.length < 4) {
            logger.info("bad line {}", line);
        }
        return cols[1] + "." + cols[2];
    }

    @Test
    public void testSimple() throws Exception {

        long updateTimeAt = System.currentTimeMillis();
        logger.info("updateTimeAt {}", updateTimeAt);

        List<String> deals = new ArrayList<>();
        deals.add("0\tMBC\t100\tKOR\t300\t400");
       // deals.add("1\tEBS\t100\tENG\t300\t400");
        //deals.add("0\tMBC\t100\tJPN\t300\t400");
        //deals.add("0\tMBC\t100\tCHN\t300\t400");

        HashDbEvent hashDbEvent = new HashDbEvent() {
            @Override
            public void onIndex(String key, String serializeValue) {
                SimpleHashDbValue simpleHashDbValue = new SimpleHashDbValue(serializeValue);
                if (simpleHashDbValue.getUpdateTimeAt() == updateTimeAt) {
                    logger.info("new index {} ", key);
                } else {
                    logger.info("old index {} ", key);
                }
            }

            @Override
            public boolean onOverWrite(String oldSerializeValue, String newSerializeValue) {
                SimpleHashDbValue oldValue = new SimpleHashDbValue(oldSerializeValue);
                SimpleHashDbValue newValue = new SimpleHashDbValue(newSerializeValue);
                return false;
            }
        };

        try (HashDbWriter hashDbWriter = new HashDbWriter("C:\\temp\\rr", 1, hashDbEvent, false, false);) {
            deals.forEach(line -> {
                hashDbWriter.put(getNamedKey(line), new SimpleHashDbValue(line, updateTimeAt));
            });
        }
    }

    @Test
    public void testFile() throws Exception {
        long updateTimeAt = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        logger.info("updateTimeAt {}", updateTimeAt);

        HashDbEvent hashDbEvent = new HashDbEvent() {
            @Override
            public void onIndex(String key, String serializeValue) {

            }

            @Override
            public boolean onOverWrite(String oldSerializeValue, String newSerializeValue) {
                return true;
            }
        };

        try (HashDbWriter hashDbWriter = new HashDbWriter("C:\\temp\\rr", 2048, hashDbEvent, true, false);) {
            int lineCount = 0;
            String line, trimLine ;
            String readPath = "C:\\temp\\naver_all.txt";

            try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8);) {
                line = in.readLine();
                while ((line = in.readLine()) != null) {
                    lineCount++;
                    hashDbWriter.put(getNamedKey(line), new SimpleHashDbValue(line, updateTimeAt));
                    if (lineCount % 200_000 == 0) {
                        logger.info("process {}", lineCount);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        stopWatch.stop();
        logger.info("total elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
