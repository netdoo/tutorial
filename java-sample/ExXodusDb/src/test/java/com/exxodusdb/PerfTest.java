package com.exxodusdb;

import com.exxodusdb.domain.EPTSVData;
import com.exxodusdb.domain.EPTSVData2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Created by jhkwon78 on 2017-11-17.
 */
public class PerfTest {

    final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testPerf() throws Exception {

        long lineCount = 0;
        String line;
        String readPath = "C:\\temp\\naver_all.txt";
        boolean isAllEP = true;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8)) {
            while ((line = in.readLine()) != null) {
                lineCount++;

                if (StringUtils.startsWithIgnoreCase(line, "id\t")) {
                    // 헤더 라인은 무시함.
                    continue;
                }

                try {
                    EPTSVData curr = new EPTSVData(line, isAllEP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        LOGGER.info("as is elapsed time 1 {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));

        stopWatch.reset();
        stopWatch.start();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8)) {
            while ((line = in.readLine()) != null) {
                lineCount++;

                if (StringUtils.startsWithIgnoreCase(line, "id\t")) {
                    // 헤더 라인은 무시함.
                    continue;
                }

                try {
                    EPTSVData2 curr = new EPTSVData2(line, isAllEP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        LOGGER.info("to be elapsed time 2 {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));

    }
}
