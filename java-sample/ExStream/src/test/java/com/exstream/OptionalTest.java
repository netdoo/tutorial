package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OptionalTest {

    final Logger logger = LoggerFactory.getLogger(OptionalTest.class);

    String getNullText() {
        return null;
    }

    String getNotNullText() {
        return new String("hello");
    }

    @Test
    public void _01_NullTest() {
        // OptionalTest:27 - empty
        logger.info("{}", Optional.ofNullable(getNullText()).map(String::toUpperCase).orElse("empty"));
    }

    @Test
    public void _02_NotNullTest() {
        // OptionalTest:32 - HELLO
        logger.info("{}", Optional.ofNullable(getNotNullText()).map(String::toUpperCase).orElse("empty"));
    }
}
