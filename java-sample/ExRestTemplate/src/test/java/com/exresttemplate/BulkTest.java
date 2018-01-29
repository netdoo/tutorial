package com.exresttemplate;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 *
 [실행결과]
 2018-01-29 14:19:06.287 INFO  BulkTest:65 - [NGINX WITH FILE] thread 20 request 500 elapsed time 14 (secs)
 2018-01-29 14:19:14.135 INFO  BulkTest:85 - [REDIS WITH SPRING] thread 20 request 500 elapsed time 7 (secs)
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    final static int MAX_REQUEST_COUNT = 500;
    final static int MAX_THREAD_COUNT = 20;

    @Test
    public void _0_테스트_준비() throws Exception {

    }

    void bulkRequest(RestTemplate restTemplate, String url, int maxThreadCount, int maxRequestCount, CountDownLatch countDownLatch) {
        IntStream.range(0, maxThreadCount).forEach(threadCount -> {
            Thread thread = new Thread(() -> {
                IntStream.range(0, maxRequestCount).forEach(requestCount -> {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                });
                countDownLatch.countDown();
            });
            thread.start();
        });
    }

    @Test
    public void _1_벌크_요청_테스트() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String baseUrl = "http://localhost:8090";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/README.txt")                          // Add path
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREAD_COUNT);
        bulkRequest(restTemplate, url, MAX_THREAD_COUNT, MAX_REQUEST_COUNT, countDownLatch);
        countDownLatch.await(10, TimeUnit.HOURS);
        stopWatch.stop();
        logger.info("[NGINX WITH FILE] thread {} request {} elapsed time {} (secs)", MAX_THREAD_COUNT, MAX_REQUEST_COUNT, stopWatch.getTime(TimeUnit.SECONDS));
    }

    @Test
    public void _2_벌크_요청_테스트() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String baseUrl = "http://localhost:8080";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/bulk")                          // Add path
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREAD_COUNT);
        bulkRequest(restTemplate, url, MAX_THREAD_COUNT, MAX_REQUEST_COUNT, countDownLatch);
        countDownLatch.await(10, TimeUnit.HOURS);
        stopWatch.stop();
        logger.info("[REDIS WITH SPRING] thread {} request {} elapsed time {} (secs)", MAX_THREAD_COUNT, MAX_REQUEST_COUNT, stopWatch.getTime(TimeUnit.SECONDS));
    }
}
