package com.exre;


import com.google.re2j.Pattern;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SplitTest {

    final Logger logger = LoggerFactory.getLogger(SplitTest.class);

    Set<String> loadPattern(String path) {
        Set<String> pattern = new HashSet<>();
        try {
            Files.lines(Paths.get(path)).forEach(line -> {
                pattern.add(line);
            });
        } catch (Exception e) {

        }
        return pattern;
    }


    String replacePattern(String input) {

        Set<String> colorPattern = loadPattern("C:\\Temp\\colorPattern.txt");
        Set<String> sizePattern = loadPattern("C:\\Temp\\sizePattern.txt");
        List<String> termList = new ArrayList<>();
        String terms[] = input.split(" ");

        for (String term : terms) {
            if (colorPattern.contains(term)) {
                termList.add("#color#");
            } else if (sizePattern.contains(term)) {
                termList.add("#size#");
            } else {
                termList.add(term);
            }
        }

        return Strings.join(termList, '.');
    }


    @Test
    public void testReplace() {
        List<String> inputList = new ArrayList<>();

        inputList.add("나이키 운동화 블루 260mm");
        inputList.add("나이키 운동화 Blue 260mm");
        inputList.add("나이키 운동화 파란색 270mm");

        inputList.forEach(input -> {
            logger.info("{} => {}", input, replacePattern(input));
        });
    }

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
