package com.exre;


import com.google.re2j.Pattern;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

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

    void testDefRegEx(String patternText, String searchText) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        java.util.regex.Pattern defPattern = java.util.regex.Pattern.compile(patternText);
        Matcher matcher = defPattern.matcher(searchText);
        boolean result = matcher.find();
        stopWatch.stop();
        logger.info("java result {} elapsed time {} (ms)", result, stopWatch.getTime(TimeUnit.MILLISECONDS)); // true
    }

    void testGoogleRegEx(String patternText, String searchText) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Pattern pattern = Pattern.compile(patternText);
        boolean result = pattern.matches(searchText);
        stopWatch.stop();
        logger.info("google result {} elapsed time {} (ms)", result, stopWatch.getTime(TimeUnit.MILLISECONDS)); // true
    }

    void testSet(Set<String> dic, String searchText) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean result = dic.contains(searchText);
        stopWatch.stop();
        logger.info("set result {} elapsed time {} (ms)", result, stopWatch.getTime(TimeUnit.MILLISECONDS)); // true
    }

    @Test
    public void testLargeOrPattern() {
        String searchText = "000099999";
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> dic = new HashSet<>();

        for (int i = 0; i < 100_000; i++) {
            stringBuilder.append("^").append("0000").append(i).append("$|");
            dic.add("0000"+i);
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        String patternText = stringBuilder.toString();
        //logger.info("patternText {}", patternText);

        testDefRegEx(patternText, searchText);
        testGoogleRegEx(patternText, searchText);
        testSet(dic, searchText);
        testDefRegEx(patternText, searchText);
        testGoogleRegEx(patternText, searchText);
        testSet(dic, searchText);
    }
}
