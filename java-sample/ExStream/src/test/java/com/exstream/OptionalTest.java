package com.exstream;

import com.exstream.domain.Box;
import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OptionalTest {

    final Logger logger = LoggerFactory.getLogger(OptionalTest.class);
    private Box defaultBox = new Box("white");

    Box getNullBox() {
        return null;
    }

    Box getNotNullBox() {
        return new Box("orange");
    }

    @Test
    public void _01_Null_orElseGet_Test() {
        /*
        begin _01_Null_orElseGet_Test
        Box 생성자 호출됨
        Box {color : 'null'}
        finish _01_Null_orElseGet_Test
        */
        logger.info("begin _01_Null_orElseGet_Test");
        logger.info("{}", Optional.ofNullable(getNullBox()).map(box -> {
            box.setColor(box.getColor().toUpperCase());
            return box;
        }).orElseGet(Box::new));
        logger.info("finish _01_Null_orElseGet_Test");
    }

    @Test
    public void _02_NotNull_orElseGet_Test() {
        /*
        begin _02_NotNull_orElseGet_Test
        Box {color : 'ORANGE'}
        finish _02_NotNull_orElseGet_Test
        */
        logger.info("begin _02_NotNull_orElseGet_Test");
        logger.info("{}", Optional.ofNullable(getNotNullBox()).map(box -> {
            box.setColor(box.getColor().toUpperCase());
            return box;
        }).orElseGet(Box::new));
        logger.info("finish _02_NotNull_orElseGet_Test");
    }

    @Test
    public void _03_Null_orElse_Test() {
        /*
        begin _03_Null_orElse_Test
        Box 생성자 호출됨
        Box {color : 'null'}
        finish _03_Null_orElse_Test
        */
        logger.info("begin _03_Null_orElse_Test");
        logger.info("{}", Optional.ofNullable(getNullBox()).map(box -> {
            box.setColor(box.getColor().toUpperCase());
            return box;
        }).orElse(new Box()));
        logger.info("finish _03_Null_orElse_Test");
    }

    @Test
    public void _04_NotNull_orElse_Test() {
        /*
        begin _04_NotNull_orElse_Test
        Box 생성자 호출됨
        Box {color : 'ORANGE'}
        finish _04_NotNull_orElse_Test
        */
        logger.info("begin _04_NotNull_orElse_Test");
        logger.info("{}", Optional.ofNullable(getNotNullBox()).map(box -> {
            box.setColor(box.getColor().toUpperCase());
            return box;
        }).orElse(new Box()));
        logger.info("finish _04_NotNull_orElse_Test");
    }

    @Test
    public void _05_Null_orElseDefault_Test() {
        /*
        begin _05_Null_orElseDefault_Test
        Box {color : 'white'}
        finish _05_Null_orElseDefault_Test
        */
        logger.info("begin _05_Null_orElseDefault_Test");
        logger.info("{}", Optional.ofNullable(getNullBox()).map(box -> {
            box.setColor(box.getColor().toUpperCase());
            return box;
        }).orElse(defaultBox));
        logger.info("finish _05_Null_orElseDefault_Test");
    }

    @Test
    public void _06_Split_Test() {
        String empty = "";
        String emptyResult[] = Optional.ofNullable(empty).orElse("").split("\\.");
        String result = emptyResult[emptyResult.length-1];

        logger.info("result {}", result);
    }
}
