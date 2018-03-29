package com.exfailsafe;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static int rand() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        if (now.getSecond() % 10 == 0) {
            return Math.abs(new Random().nextInt() % 10);
        } else {
            logger.info("simulate exception");
            throw new RuntimeException("simulate exception");
        }
    }

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new RetryPolicy()
                .retryOn(ConnectException.class, RuntimeException.class)
                .withDelay(1, TimeUnit.SECONDS)
                .withMaxRetries(30);

        logger.info("Run with retries, without return value");
        Failsafe.with(retryPolicy).run(() -> rand());
        logger.info("==================================================");

        TimeUnit.SECONDS.sleep(1);
        logger.info("Get with retries");
        int rand = Failsafe.with(retryPolicy).get(() -> rand());
        logger.info("rand {}", rand);
    }
}
