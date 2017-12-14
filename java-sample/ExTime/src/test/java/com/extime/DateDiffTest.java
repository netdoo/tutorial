package com.extime;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DateDiffTest {

    final static Logger logger = LoggerFactory.getLogger(DateDiffTest.class);

    @Test
    public void _1_날짜_차이_비교() {
        LocalDate oldDate = LocalDate.of(2017, 12, 12);
        LocalDate newDate = LocalDate.of(2017, 12, 20);

        // old 에서 new 까지의 날짜 차이 계산
        Period period = oldDate.until(newDate);
        assertThat(period.getYears(), is(0));
        assertThat(period.getMonths(), is(0));
        assertThat(period.getDays(), is(8));
    }

    @Test
    public void _2_날짜_시간_차이_비교() {
        LocalDateTime oldDateTime = LocalDateTime.of(2017, 12, 12, 10, 10, 10);
        LocalDateTime newDateTime = LocalDateTime.of(2017, 12, 12, 10, 12, 10);

        // old 에서 new 까지의 날짜 차이 계산
        long days = ChronoUnit.DAYS.between(oldDateTime, newDateTime);
        long hours = ChronoUnit.HOURS.between(oldDateTime, newDateTime);
        long mins = ChronoUnit.MINUTES.between(oldDateTime, newDateTime);

        assertThat(mins, is(2L));
    }
}

