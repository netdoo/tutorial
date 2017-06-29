package com.exasyncresttemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpServerErrorException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncBulkHttp<T> {

    final static int DEF_MAX_RETRY = 30;
    final static int DEF_LIMIT_REQUEST = 30;
    final static long DEF_RETRY_TERM_MILLS = 500;

    ConcurrentHashMap<AsyncBulkHttpRequestBuilder, ResponseEntity<T>> requests;
    AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    ResponseEntity<T> internalServerError = new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);

    int maxRetry = DEF_MAX_RETRY;
    int limitRequest = DEF_LIMIT_REQUEST;
    long retryTermMills = DEF_RETRY_TERM_MILLS;
    final Class<T> responseType;

    public AsyncBulkHttp(Class<T> responseType) {
        requests = new ConcurrentHashMap<>();
        this.responseType = responseType;
    }

    ConcurrentHashMap<AsyncBulkHttpRequestBuilder, ResponseEntity<T>> get() {
        return this.requests;
    }

    public  void addRequest(AsyncBulkHttpRequestBuilder builder) {
        requests.put(builder, this.internalServerError);
    }

    public void clear() {
        this.requests.clear();
    }

    private  void setSuccess(AsyncBulkHttpRequestBuilder builder, ResponseEntity<T> responseEntity) {         requests.put(builder, responseEntity);    }
    private void setFailure(AsyncBulkHttpRequestBuilder builder) {
        requests.put(builder, this.internalServerError);
    }
    private void setFailure(AsyncBulkHttpRequestBuilder builder, ResponseEntity<T> failure) {        requests.put(builder, failure);    }

    public AsyncBulkHttp<T> setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
        return this;
    }

    public int getMaxRetry() {
        return this.maxRetry;
    }

    public AsyncBulkHttp<T> setRetryTerm(long retryTermMills) {
        this.retryTermMills = retryTermMills;
        return this;
    }

    public long getRetryTerm() {
        return this.retryTermMills;
    }

    public void setLimitRequest(int limitRequest) {
        this.limitRequest = limitRequest;
    }
    public int getLimitRequest() {
        return this.limitRequest;
    }

    public void requestAll() {
        bulkRequestAll();
    }

    public void requestAll(List<AsyncBulkHttpRequestBuilder> requestBuilders) {
        requestBuilders.stream().forEach(builder -> addRequest(builder));
        bulkRequestAll();
    }

    private void bulkRequestAll() {

        final int calcMaxRetry = (this.requests.size() / this.limitRequest) + this.maxRetry;

        for (int retry = 0; retry < calcMaxRetry; retry++) {

            List<AsyncBulkHttpRequestBuilder> requestBuilders = this.requests.entrySet().stream()
                        .filter(e -> e.getValue().getStatusCode() != HttpStatus.OK )
                        .map(Map.Entry::getKey).limit(this.limitRequest).collect(Collectors.toList());

            if (requestBuilders.size() == 0)
                break;

            CountDownLatch countDownLatch = new CountDownLatch(requestBuilders.size());

            requestBuilders.stream().forEach(builder -> {
                ListenableFuture<ResponseEntity<T>> entity = this.asyncRestTemplate.exchange(builder.getUrl(), builder.getMethod(), builder.getRequestEntity(), this.responseType);
                entity.addCallback(new AsyncHttpCallback<T>(builder, countDownLatch, this));
            });

            try { countDownLatch.await(requestBuilders.size() * 10, TimeUnit.SECONDS); } catch (Exception e) {}
            try { Thread.sleep(this.retryTermMills);            } catch (Exception e) {}
        }
    }

    private class AsyncHttpCallback<T> implements ListenableFutureCallback<ResponseEntity<T>> {
        final AsyncBulkHttpRequestBuilder builder;
        final CountDownLatch countDownLatch;
        final AsyncBulkHttp<T> asyncHttp;

        public AsyncHttpCallback(AsyncBulkHttpRequestBuilder builder, CountDownLatch countDownLatch, AsyncBulkHttp<T> asyncHttp){
            this.builder = builder;
            this.countDownLatch = countDownLatch;
            this.asyncHttp = asyncHttp;
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable instanceof HttpServerErrorException) {
                HttpServerErrorException e = (HttpServerErrorException) throwable;
                asyncHttp.setFailure(this.builder, new ResponseEntity<T>(e.getStatusCode()));
            } else {
                asyncHttp.setFailure(this.builder);
            }

            countDownLatch.countDown();
        }

        @Override
        public void onSuccess(ResponseEntity<T> responseEntity) {
            asyncHttp.setSuccess(this.builder, responseEntity);
            countDownLatch.countDown();
        }
    }
}