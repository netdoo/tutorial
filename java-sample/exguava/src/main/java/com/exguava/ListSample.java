package com.exguava;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ListSample {

    final static Logger logger = LoggerFactory.getLogger(ListSample.class);

    public static void main( String[] args ) {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<List<Integer>> pages = Lists.partition(lists, 3);
        logger.info("{}", pages);

        for (List<Integer> page : Lists.partition(lists, 3)) {
            logger.info("{}", page);
        }
    }
}
