package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntStreamTest {

    final Logger LOGGER = LoggerFactory.getLogger(IntStreamTest.class);

    @Test
    public void testApp() {

        List<Integer> result = IntStream.range(1, 5).mapToObj(i -> i * i).collect(Collectors.toList());
        LOGGER.info("{}", result);

    }
}
