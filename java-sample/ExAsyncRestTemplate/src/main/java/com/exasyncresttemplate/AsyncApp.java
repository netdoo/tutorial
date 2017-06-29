package com.exasyncresttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncApp {
    final static Logger logger = LoggerFactory.getLogger(AsyncApp.class);

    public static void main( String[] args ) throws Exception {
        int i = 0;
        AsyncBulkHttp<String> asyncHttp = new AsyncBulkHttp<>(String.class);

        asyncHttp.setMaxRetry(1000)
                .setRetryTerm(0);

        for (i = 0; i < 50_000; i++) {
            asyncHttp.addRequest(new AsyncBulkHttpRequestBuilder("http://localhost:8080/echo?message=hello" + Integer.toString(i), HttpMethod.GET));
        }

        for (i = 0; i < 10_000; i++) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.set("message", "test post" + Integer.toString(i));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.add("X-Authorization", "API키값");
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
            asyncHttp.addRequest(new AsyncBulkHttpRequestBuilder("http://localhost:8080/postecho", HttpMethod.POST, httpEntity));
        }

        /// Let's go kill the http server !!
        asyncHttp.requestAll();

        ConcurrentHashMap<AsyncBulkHttpRequestBuilder, ResponseEntity<String>> requests = asyncHttp.get();

        String path = "C:\\temp\\sample.txt";
        File f = new File(path);
        f.delete();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

        requests.forEach((request, responseEntity) -> {
            try {
                bufferedWriter.write(request.getUrl() + "   "  + responseEntity.getBody() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bufferedWriter.close();
    }
}
