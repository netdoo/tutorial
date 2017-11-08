package com.exresttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class GetSample {

    final static Logger logger = LoggerFactory.getLogger(GetSample.class);
    final static RestTemplate restTemplate = new RestTemplate();

    static void simpleGet(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        logger.info("{}", responseEntity.getBody());
    }

    static void exchangeGet(String url) {
        ResponseEntity<List<Comment>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
        });
        List<Comment> commentList = responseEntity.getBody();
        logger.info("GET : {}", commentList);
    }

    static void getForObject(String url) {
        String body = restTemplate.getForObject(url, String.class);
        logger.info("{}", body);
    }

    static void getForEntity(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        logger.info("{}", responseEntity.getBody());
    }

    public static void main( String[] args ) {
        String baseUrl = "https://jsonplaceholder.typicode.com";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/comments")                             // Add path
                .queryParam("postId", 1)                       // Add one or more query params
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        simpleGet(url);
        getForObject(url);
        getForEntity(url);
        exchangeGet(url);
    }
}
