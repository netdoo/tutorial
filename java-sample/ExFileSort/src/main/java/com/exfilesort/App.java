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
            out.println("11,AAA");
            out.println("6,BBB");
            out.println("7,가가가");
            out.println("13,나나나");
            out.println("8,ZZZ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static long parseId(String text) {
        String cols[] = text.split(",");
        return Long.valueOf(cols[0]);
    }

    static String parseName(String text) {
        String cols[] = text.split(",");
        return cols[1];
    }

    static class DescIdComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            long leftId = parseId(o1);
            long rightId = parseId(o2);

            if (leftId > rightId) {
                return -1;
            } else if (leftId == rightId) {
                return 0;
            }

            return 1;
        }

        @Override
        public boolean equals(Object obj) {  return false;  }
    }

    static class DescNameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String leftName = parseName(o1);
            String rightName = parseName(o2);

            return leftName.compareTo(rightName);
        }

        @Override
        public boolean equals(Object obj) {  return false;  }
    }

    static class AscIdComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            long leftId = parseId(o1);
            long rightId = parseId(o2);

            if (leftId < rightId) {
                return -1;
            } else if (leftId == rightId) {
                return 0;
            }

            return 1;
        }

        @Override
        public boolean equals(Object obj) {  return false;  }
    }

    static class AscNameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String leftName = parseName(o1);
            String rightName = parseName(o2);

            return rightName.compareTo(leftName);
        }

        @Override
        public boolean equals(Object obj) {  return false;  }
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

        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new AscIdComparator()), new File("C:\\Temp\\dummy_asc_id_sort.txt"));
        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new DescIdComparator()), new File("C:\\Temp\\dummy_desc_id_sort.txt"));
        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new AscNameComparator()), new File("C:\\Temp\\dummy_asc_name_sort.txt"));
        ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(dummyFile, new DescNameComparator()), new File("C:\\Temp\\dummy_desc_name_sort.txt"));

        logger.info("== id asc sort result ==");
        printFile("C:\\Temp\\dummy_asc_id_sort.txt");

        logger.info("== id desc sort result ==");
        printFile("C:\\Temp\\dummy_desc_id_sort.txt");

        logger.info("== name desc sort result ==");
        printFile("C:\\Temp\\dummy_desc_name_sort.txt");

        logger.info("== name asc sort result ==");
        printFile("C:\\Temp\\dummy_asc_name_sort.txt");
    }
}

