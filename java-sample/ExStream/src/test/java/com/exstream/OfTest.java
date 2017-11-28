package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OfTest {

    final Logger logger = LoggerFactory.getLogger(OfTest.class);

    @Test
    public void testOf() {
        String text = "foo";
        String result = Stream.of(text).map(param -> param.toUpperCase()).findAny().get();
        logger.info("{}", result);
    }
}
