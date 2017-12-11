package com.exstream;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlatMapTest {

    final Logger logger = LoggerFactory.getLogger(FlatMapTest.class);

    @Test
    public void testFlatMap() {
        String line = "RED,DARK_RED,LIGHT_RED\tGREEN,DarkGreen\tBLUE,LightBlue";
        String colors[] = line.split("\t");

        SortedSet<String> sortedColors = Arrays.stream(colors)
                .filter(color -> StringUtils.startsWith(color, "RED"))
                .peek(color -> logger.info("{}", color))                // RED,DARK_RED,LIGHT_RED
                .flatMap(redColor -> Arrays.stream(redColor.split(",")))    // [RED,DARK_RED,LIGHT_RED]
                .peek(redColor -> logger.info("{}", redColor))
                .filter(redColor -> StringUtils.startsWith(redColor, "LIGHT"))
                .flatMap(redColor -> Arrays.stream(redColor.split("_")))            // [LIGHT, RED]
                .peek(finalColor -> logger.info("{}", finalColor))
                .collect(Collectors.toCollection(TreeSet::new));

        logger.info("{}", sortedColors);
    }
}
