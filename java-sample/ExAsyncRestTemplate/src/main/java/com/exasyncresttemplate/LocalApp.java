package com.exasyncresttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StopWatch;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalApp {
    final static Logger logger = LoggerFactory.getLogger(LocalApp.class);

    public static void async(String url) throws Exception {
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        try {
            Future<ResponseEntity<String>> entity = asyncRestTemplate.getForEntity(url, String.class);

            while (!entity.isDone()) {
                logger.info("waiting..");
                Thread.sleep(500);
            }

            ResponseEntity<String> responseEntity = entity.get();
            logger.info("async code {}, body {}", responseEntity.getStatusCode(), responseEntity.getBody());
        } catch(Exception e) {
            logger.info("e {}", e.getMessage());
        }
    }



    public static void main( String[] args ) throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int MAX_DATA_COUNT = 20000;
        ExecutorService p = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(MAX_DATA_COUNT);
        ArrayList<RequestRunnable> jobs = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for(int i = 0 ; i < MAX_DATA_COUNT; i++) {
            RequestRunnable requestRunnable = new RequestRunnable(i, countDownLatch);
            jobs.add(requestRunnable);
            p.execute(requestRunnable);
        }

        Runtime r = Runtime.getRuntime();

        while (countDownLatch.getCount() > 0) {
            logger.info("remain job count {}, max memory {}, used memory {}, active thread count {} ", countDownLatch.getCount(), r.maxMemory(), r.totalMemory(), Thread.activeCount());
            Thread.sleep(10_000);
        }

        logger.info("===================================fetch ");

        for (RequestRunnable requestRunnable : jobs) {
            if (requestRunnable.getResponseEntity().getStatusCode() != HttpStatus.OK) {
                logger.error("######## ");
            }
        }

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        p.shutdown();

        /// 추가된 task가 완료될때까지 10분간 기다림.
        p.awaitTermination(10, TimeUnit.MINUTES);
        stopWatch.stop();

        logger.info("elapsed time {} min", stopWatch.getTotalTimeSeconds() / 60);
    }
}
