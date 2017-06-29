package com.exasyncresttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CountDownLatch;


public class RequestRunnable implements Runnable {

    final static Logger logger = LoggerFactory.getLogger(RequestRunnable.class);

    int id;
    ResponseEntity<String> responseEntity;
    CountDownLatch countDownLatch;
    public RequestRunnable(int id, CountDownLatch countDownLatch) {
        this.id = id;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        Http http = new Http();
        String url = "http://localhost:9999/echo?message=" + Integer.toString(this.id);
        this.responseEntity = http.getForEntity(url, String.class);

        if (this.responseEntity.getStatusCode() == HttpStatus.OK) {
            //logger.info("{}", this.responseEntity.getBody());
        } else {
            logger.info("error {}", this.responseEntity.getStatusCode());
        }

        countDownLatch.countDown();
    }

    public ResponseEntity<String> getResponseEntity() {
        return this.responseEntity;
    }
}
