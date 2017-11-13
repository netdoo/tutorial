package com.exstring;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void split() {
        String fullText = "mbc\tsbs\tkbs";
        String results[] = fullText.split("\t");

        Arrays.stream(results).forEach(text -> {
            logger.info("{}", text);
        });
    }

    public static void main( String[] args ) {
        split();
    }
}

