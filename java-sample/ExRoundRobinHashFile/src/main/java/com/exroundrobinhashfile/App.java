package com.exroundrobinhashfile;

import com.sleepycat.je.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    final static String rrDir = "./rr";
    final static String dbDir = "./db";

    static String getNamedKey(String line) {
        String cols[] = line.split("\t");
        if (cols.length < 4) {
            logger.info("bad line {}", line);
        }
        return cols[1] + "." + cols[2];
    }

    static class CustomComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String leftNamedKey = getNamedKey(o1);
            String rightNameedKey = getNamedKey(o2);

            return leftNamedKey.compareTo(rightNameedKey);
        }

        @Override
        public boolean equals(Object obj) {  return false;  }
    }

    public static void main( String[] args ) throws Exception {

        String line, trimLine ;
        String readPath = "C:\\temp\\naver_all.txt";
        BufferedBerkeleyDB db = new BufferedBerkeleyDB(new BerkeleyDB(dbDir, "bdb", false), 100_000);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 10; i++) {
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
            logger.info("{} process elapsed time {} (secs)", i, subStopWatch.getTime(TimeUnit.SECONDS));
        }

        stopWatch.stop();
        logger.info("total elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
        db.close();
    }
}

