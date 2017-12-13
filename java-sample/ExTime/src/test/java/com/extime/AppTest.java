package com.extime;


import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertTrue;

public class AppTest {

    @Test
    public void AppTest() {
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
}

