package com.exchroniclemap;

import net.openhft.chronicle.map.ChronicleMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void testCrudSample() {

        File file = new File("C:\\temp\\cmap.dat");

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                // Entry checksums make sense only for persisted Chronicle Maps, and are ON by
                // default for such maps
                .entries(1)
                .averageKeySize(100)
                .averageValueSize(100)
                .createPersistedTo(file)) {

            map.put("001", "RED");
            map.put("002", "BLUE");

            logger.info("find {}", map.get("001"));

            map.put("001", "LightRED");

            logger.info("find {}", map.get("001"));

            map.remove("001");
            logger.info("find {}", map.get("001"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void bulkInsert() {
        File file = new File("C:\\temp\\cmap.dat");

        if (file.exists())
            file.delete();

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                // Entry checksums make sense only for persisted Chronicle Maps, and are ON by
                // default for such maps
                .entries(1_000_000)
                .averageKeySize(100)
                .averageValueSize(100)
                .createPersistedTo(file)) {

            for (int i = 0; i < 100_000; i++) {
                map.put("123456789"+i, "012345678901234567890123456789");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }

    static void printInfo() {
        File file = new File("C:\\temp\\cmap.dat");

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                // Entry checksums make sense only for persisted Chronicle Maps, and are ON by
                // default for such maps
                .entries(1_000_000)
                .averageKeySize(100)
                .averageValueSize(100)
                .createPersistedTo(file)) {

            logger.info("size {}", map.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        //testCrudSample();
        //bulkInsert();
        printInfo();
    }
}

