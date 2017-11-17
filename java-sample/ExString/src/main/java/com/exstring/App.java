package com.exstring;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void splitCase1() {
        String fullText = "mbc\tsbs\tkbs";
        String results[] = fullText.split("\t");    // 3
        logger.info("length {}", results.length);

        Arrays.stream(results).forEach(text -> {
            logger.info("{}", text);
        });

        Arrays.sort(results);

        logger.info("== sort ==");

        Arrays.stream(results).forEach(text -> {
            logger.info("{}", text);
        });
    }

    static void splitCase2() {
        String fullText = "\t\t\t";
        String results[] = fullText.split("\t");    // 1

        logger.info("length {}", results.length);

        Arrays.stream(results).forEach(text -> {
            logger.info("{}", text);
        });
    }

    public static void main( String[] args ) {
        splitCase1();
        splitCase2();
    }
}

