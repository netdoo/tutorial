package com.exfilesort;

import com.google.code.externalsorting.ExternalSort;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void createDummyFile(File dummyFile) throws Exception {

        if (dummyFile.exists()) {
            dummyFile.delete();
        }

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(dummyFile.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW));) {
            out.println("11,MBC");
            out.println("6,SBS");
            out.println("7,KBS");
            out.println("13,EBS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class DescComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String left[] = o1.split(",");
            String right[] = o2.split(",");
            long leftId = Long.valueOf(left[0]);
            long rightId = Long.valueOf(right[0]);

            if (leftId > rightId) {
                return -1;
            } else if (leftId == rightId) {
                return 0;
            }

            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

    }

    static class AscComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            String left[] = o1.split(",");
            String right[] = o2.split(",");
            long leftId = Long.valueOf(left[0]);
            long rightId = Long.valueOf(right[0]);

            if (leftId < rightId) {
                return -1;
            } else if (leftId == rightId) {
                return 0;
            }

            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    static void printFile(String path) throws Exception {
        String line;
        try (BufferedReader in = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);) {
            while ((line=in.readLine()) != null) {
                logger.info("{}", line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws Exception {
        File dummyFile = new File("C:\\Temp\\dummy.txt");
        createDummyFile(dummyFile);

        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new AscComparator()), new File("C:\\Temp\\dummy_asc_sort.txt"));
        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new DescComparator()), new File("C:\\Temp\\dummy_desc_sort.txt"));

        logger.info("== asc sort result ==");
        printFile("C:\\Temp\\dummy_asc_sort.txt");

        logger.info("== desc sort result ==");
        printFile("C:\\Temp\\dummy_desc_sort.txt");
    }
}

