package com.excollection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImmutablListTest {
    final static Logger logger = LoggerFactory.getLogger(ImmutablListTest.class);

    @Test
    public void _0_테스트_준비() throws Exception {
    }

    @Test
    public void _1_UnsupportedOperationException_테스트() throws Exception {
        List<String> mutableList = new ArrayList<>();
        mutableList.add("1");

        try {
            List<String> immutableList = Collections.unmodifiableList(new ArrayList<>(mutableList));
            immutableList.add("a");
        } catch (UnsupportedOperationException e) {
            logger.info("catch UnsupportedOperationException : ", e);
        } catch (Exception e) {
            logger.info("catch Exception : ", e);
        }
    }

    @Test
    public void _2_기본_테스트() throws Exception {
        List<String> mutableList = new ArrayList<>();

        mutableList.add("1");
        mutableList.add("2");
        mutableList.add("3");

        List<String> firstImmutableList = Collections.unmodifiableList(mutableList);
        List<String> secondImmutableList = Collections.unmodifiableList(new ArrayList<>(mutableList));

        mutableList.add("4");
        mutableList.add("5");

        logger.info("list => {}", mutableList);             //  => [1, 2, 3, 4, 5]
        logger.info("first => {}", firstImmutableList);     //  => [1, 2, 3, 4, 5]
        logger.info("second => {}", secondImmutableList);   //  => [1, 2, 3]
    }
}
