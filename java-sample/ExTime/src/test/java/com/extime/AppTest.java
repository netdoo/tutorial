package com.extime;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void _01_ParseTest() {
        LocalDate oldDate = LocalDate.of(2017, 12, 12);
        LocalDate newDate = LocalDate.of(2017, 12, 20);

        assertTrue(oldDate.isBefore(newDate));  // 2017/12/12일이 newDate 이전이면 true
        assertTrue(newDate.isAfter(oldDate));   // 2017/12/20일이 oldDate 이후라면 true
        assertTrue(newDate.isEqual(LocalDate.of(2017, 12, 20)));    // newDate가 2017/12/20일 이라면 true

        // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")); // 2010-11-25T12:30

        assertTrue(newDate.isEqual(LocalDate.parse("20171220", DateTimeFormatter.ofPattern("yyyyMMdd"))));
        assertTrue(newDate.isEqual(LocalDate.parse("2017/12/20", DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
        assertTrue(newDate.isEqual(LocalDate.parse("2017-12-20")));
    }

    @Test
    public void _02_PlusTest() {
        LocalDateTime now = LocalDateTime.of(2018, 3, 13, 23,59,59);
        LocalDateTime result = now.plusSeconds(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        logger.info("{} + 1sec => {}", now.format(formatter), result.format(formatter));
    }
}

