package com.exresttemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class App {
    final Logger logger = LoggerFactory.getLogger(getClass());

    public void get() {
        String baseUrl = "https://jsonplaceholder.typicode.com";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl= UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/comments")                             // Add path
                .queryParam("postId", 1)                       // Add one or more query params
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        ResponseEntity<List<Comment>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>(){});
        List<Comment> commentList = responseEntity.getBody();

        logger.info("GET : {}", commentList);
    }

    public void getWithConverters() {
        String baseUrl = "https://jsonplaceholder.typicode.com";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl= UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/comments")                             // Add path
                .queryParam("postId", 1)                       // Add one or more query params
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();

        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        mappingJacksonHttpMessageConverter.setObjectMapper(objectMapper);
        httpMessageConverters.add(mappingJacksonHttpMessageConverter);
        restTemplate.setMessageConverters(httpMessageConverters);

        ResponseEntity<List<Comment>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>(){});
        List<Comment> commentList = responseEntity.getBody();

        logger.info("GET : {}", commentList);
    }

    void post() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.set("postId", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("X-Authorization", "API키값");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<PostResponse> responseEntity = restTemplate.exchange(url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<PostResponse>(){});

        PostResponse postResponse = responseEntity.getBody();

        logger.info("POST : {}",postResponse.toString());
    }

    public static void main( String[] args ) {
        App app = new App();
        app.get();
        app.getWithConverters();
        app.post();
    }
}
