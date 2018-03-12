package com.extime;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UTCTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void _01_Conv_LocalDateTime_To_UTC() {
        LocalDateTime localDateTime = LocalDateTime.of(2017, 3, 12, 15, 0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        LocalDateTime utcDateTime = localDateTime
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        logger.info("LocalDateTime {}", localDateTime.format(formatter));
        logger.info("UTC LocalDateTime {}", utcDateTime.format(formatter));

        // 한국 표준시(韓國標準時, KST, Korea Standard Time)는
        // 대한민국의 표준시로 UTC 보다 9시간 빠른 동경 135도(UTC+09:00)를 기준으로 하고 있다.
        // 그래서, 한국 표준시를 UTC 시간으로 변경하기 위해서는 9시간 빼주면 된다.
        assertTrue(utcDateTime.isEqual(LocalDateTime.of(2017, 3, 12, 6, 0, 0)));
    }
}

