package com.exroundrobinhashfile;

import org.apache.commons.lang3.time.StopWatch;
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

        String line, trimLine ;
        String readPath = "C:\\temp\\naver_all.txt";
        int lineCount = 0;
        RoundRobinHashFile roundRobinHashFile = new RoundRobinHashFile();
        roundRobinHashFile.open("C:\\temp\\rr", 512, false);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8);) {
            line = in.readLine();
            while ((line = in.readLine()) != null) {
                lineCount++;
                String key = getNamedKey(line);
                roundRobinHashFile.println(key, key + "\t" + line);
                if (lineCount % 100_000 == 0) {
                    logger.info("process {}", lineCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        roundRobinHashFile.close();
        stopWatch.stop();
        logger.info("total elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
