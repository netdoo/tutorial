package com.extime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static void getTimestamp() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 2015-04-18 00:42:24
        logger.info("now {}", now);
    }


    public static String getMainSep() {
        return ";";
    }

    public static String getUserSep() {
        return "/";
    }

    public static String getActionSep() {
        return "●";
    }

    public static void foo() {
        StringBuffer log = new StringBuffer();
        User user = new User();
        DealSearchWeight dealSearchWeight = new DealSearchWeight();

        log.append(user.getMemberId()).append(getUserSep()).append(user.getUsername())
                .append(getMainSep())
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append(getMainSep())
                .append("정렬 가중치 추가 (")
                .append("키워드 : ").append(String.join(";", dealSearchWeight.getKeyword())).append(",")
                .append("가중치 : ").append(dealSearchWeight.getKeywordWeight()).append(")");

        dealSearchWeight.setUpdateHistory(log.toString());
    }

    public static void main( String[] args ) throws Exception {
        diffSecs();
        diffDays();
        getTimestamp();
        foo();
        System.out.println( "Hello World!" );
    }
}
