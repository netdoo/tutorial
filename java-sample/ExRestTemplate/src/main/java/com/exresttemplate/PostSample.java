package com.exresttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class PostSample {

    final static Logger logger = LoggerFactory.getLogger(PostSample.class);
    final static RestTemplate restTemplate = new RestTemplate();

    static void simplePost() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("postId", "1");
      //  params.add("postArrayId", "1,2,3");     // 배열형태의 Param

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.set("Accept", "application/json; charset=UTF-8");
        headers.add("X-Authorization", "API키값");

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url,
                HttpMethod.POST,
                request,
                String.class);

        String response = responseEntity.getBody();

        logger.info("{}", response);
    }

    public static void main( String[] args ) {
        simplePost();
    }
}
