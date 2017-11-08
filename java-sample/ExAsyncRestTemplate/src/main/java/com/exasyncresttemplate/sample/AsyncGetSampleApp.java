
package com.exasyncresttemplate.sample;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.*;

public class AsyncGetSampleApp {
    final static Logger logger = LoggerFactory.getLogger(AsyncGetSampleApp.class);
    final static AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();


    static HttpEntity<String> httpEntity() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return new HttpEntity<String>("parameters", requestHeaders);
    }

    static void asyncFutureGet(String url) throws Exception {
        Future<ResponseEntity<String>> futureEntity = asyncRestTemplate.exchange(url, HttpMethod.GET, httpEntity(), String.class);
     
        while (!futureEntity.isDone()) {
            logger.info("waiting..");
            Thread.sleep(500);
        }
        
        ResponseEntity<String> responseEntity = futureEntity.get();
        logger.info("Response {}", responseEntity.getBody());
    }

    static void listenableFutureGet(String url) throws Exception {

        ListenableFuture<ResponseEntity<String>> futureEntity = asyncRestTemplate.exchange(url, HttpMethod.GET, httpEntity(), String.class);
     
        while (!futureEntity.isDone()) {
            logger.info("waiting..");
            Thread.sleep(500);
        }

        ResponseEntity<String> result = futureEntity.get();
        logger.info("Response {}", result.getBody());
    }

    static void listenableFutureGet2(String url) throws Exception {

        ListenableFuture<ResponseEntity<String>> futureEntity = asyncRestTemplate.exchange(url, HttpMethod.GET, httpEntity(), String.class);

        futureEntity.addCallback(responseEntity -> {
            logger.info("success");
        }, throwable -> {
            logger.error("{}", throwable.getMessage());
        });

        while (!futureEntity.isDone()) {
            logger.info("waiting..");
            Thread.sleep(500);
        }

        ResponseEntity<String> result = futureEntity.get();
        logger.info("Response {}", result.getBody());
    }

    static class HttpListenableFuture<T> implements ListenableFutureCallback<ResponseEntity<T>> {
        final MutableBoolean error;

        public HttpListenableFuture(MutableBoolean error) {
            this.error = error;
            this.error.setFalse();
        }

        @Override
        public void onFailure(Throwable throwable) {
            error.setTrue();
            logger.error("error {}", this.error);
        }

        @Override
        public void onSuccess(ResponseEntity<T> tResponseEntity) {
            error.setFalse();
        }
    }

    static void listenableFutureGet3(String url) throws Exception {

        ListenableFuture<ResponseEntity<String>> futureEntity = asyncRestTemplate.exchange(url, HttpMethod.GET, httpEntity(), String.class);

        MutableBoolean mutableBoolean = new MutableBoolean();
        ListenableFutureCallback<ResponseEntity<String>> callback = new HttpListenableFuture<>(mutableBoolean);
        futureEntity.addCallback(callback);

        while (!futureEntity.isDone()) {
            logger.info("waiting..");
            Thread.sleep(500);
        }

        logger.info("result {}", mutableBoolean.booleanValue());
        ResponseEntity<String> result = futureEntity.get();
        logger.info("Response {}", result.getBody());
    }

    public static void main( String[] args ) throws Exception {
        
        String baseUrl = "https://jsonplaceholder.typicode.com";
        
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/comments")                             // Add path
                .queryParam("postId", 1)                       // Add one or more query params
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        
        asyncFutureGet(url);
        listenableFutureGet(url);
        listenableFutureGet2(url);
        listenableFutureGet3(url);
    }
}

