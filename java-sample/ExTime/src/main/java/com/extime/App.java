package com.extime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void diffSecs() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusSeconds(2);
        LocalDateTime finish = LocalDateTime.now();
        long elapsedSecs = ChronoUnit.SECONDS.between(start, finish);
        logger.info("elapsed {} secs", elapsedSecs);
    }

    public static void diffDays() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        long elapsedDays = Duration.between(startDate, endDate).toDays();
        logger.info("elapsed days {} (days)", elapsedDays);

        long elapsedMins = Duration.between(startDate, endDate).toMinutes();
        logger.info("elapsed minutes {} (mins)", elapsedMins);

        long elapsedSecs = ChronoUnit.SECONDS.between(startDate, endDate);
        logger.info("elapsed seconds {} (secs)", elapsedSecs);
    }

    public static void main( String[] args ) throws Exception {
        diffSecs();
        diffDays();
        System.out.println( "Hello World!" );
    }
}
