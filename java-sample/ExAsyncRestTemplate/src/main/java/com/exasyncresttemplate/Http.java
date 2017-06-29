package com.exasyncresttemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Http<T> {
    RestTemplate restTemplate;
    int retryCount;
    int retrySleepMS;

    public Http() {
        this.retryCount = 10;
        this.retrySleepMS = 100;
        this.restTemplate = new RestTemplate();
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public ResponseEntity<T> getForEntity(String url, Class<T> response)  {
        int retry = 0;
        String error;

        do {
            try {
                ResponseEntity<T> responseEntity = this.restTemplate.getForEntity(url, response);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    return responseEntity;
                }
            } catch(Exception e) {
                error = e.getMessage();
            }

            try {
                Thread.sleep(this.retrySleepMS);
            } catch(InterruptedException e) {}

        } while (retry++ < this.retryCount);

        return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
    }
}
