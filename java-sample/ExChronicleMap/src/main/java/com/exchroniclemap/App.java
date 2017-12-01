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

    static void clear() {
        File file = new File("C:\\temp\\cmap.dat");

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                // Entry checksums make sense only for persisted Chronicle Maps, and are ON by
                // default for such maps
                .entries(1_000_000)
                .averageKeySize(100)
                .averageValueSize(100)
                .createPersistedTo(file)) {

            map.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        clear();
        printInfo();
    }
}

