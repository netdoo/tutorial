package com.exroundrobinhashfile;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

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
    public void testApp() throws Exception {

        long updateTimeAt = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        logger.info("updateTimeAt {}", updateTimeAt);

        HashDbEvent hashDbEvent = (key, value) -> {
            SimpleHashDbValue simpleHashDbValue = new SimpleHashDbValue(value);
            logger.info("index {} ", key);
        };

        try (HashDbWriter hashDbWriter = new HashDbWriter("C:\\temp\\rr", 1, hashDbEvent, false, false);) {
            int lineCount = 0;
            //String line, trimLine ;
            String readPath = "C:\\temp\\naver_all.txt";

            //try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8);) {
            //    line = in.readLine();
            //    while ((line = in.readLine()) != null) {
            String line = "0\tMBC\t100\t200\t300\t400";
                    lineCount++;
                    hashDbWriter.put(getNamedKey(line), new SimpleHashDbValue(line, updateTimeAt));
                    if (lineCount % 100_000 == 0) {
                        logger.info("process {}", lineCount);
                    }
                //}
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}
        }

        stopWatch.stop();
        logger.info("total elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
