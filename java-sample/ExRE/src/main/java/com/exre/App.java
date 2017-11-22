package com.exre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.re2j.Pattern;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void testOrMatch() {
        Pattern pattern = Pattern.compile("^AAA.*|^BBB");
        logger.info("{}", pattern.matches("AAAA")); // true
        logger.info("{}", pattern.matches("AAA"));  // true
        logger.info("{}", pattern.matches("AA"));   // false
        logger.info("{}", pattern.matches("BBB"));  // true
        logger.info("{}", pattern.matches("BBBB")); // false
        logger.info("{}", pattern.matches("CCC"));  // false
    }

    public static void main( String[] args ) {
        testOrMatch();
    }
}

