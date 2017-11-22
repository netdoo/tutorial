package com.exre;


import com.google.re2j.Pattern;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegExTest {

    final Logger logger = LoggerFactory.getLogger(RegExTest.class);

    @Test
    public void testOrMatch() {
        Pattern pattern = Pattern.compile("^AAA.*|^BBB");
        logger.info("{}", pattern.matches("AAAA")); // true
        logger.info("{}", pattern.matches("AAA"));  // true
        logger.info("{}", pattern.matches("AA"));   // false
        logger.info("{}", pattern.matches("BBB"));  // true
        logger.info("{}", pattern.matches("BBBB")); // false
        logger.info("{}", pattern.matches("CCC"));  // false
    }
}
