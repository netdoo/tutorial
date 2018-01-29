package com.exresttemplate;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    final static int MAX_PUT_COUNT = 1_000;

    @Test
    public void _0_테스트_준비() throws Exception {
        
    }

    @Test
    public void _1_벌크_요청_테스트() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String baseUrl = "http://localhost:8080";
        RestTemplate restTemplate = new RestTemplate();
        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl)      // Build the base link
                .path("/README.txt")                          // Add path
                .build()                                        // Build the URL
                .encode()                                       // Encode any URI items that need to be encoded
                .toUri();                                       // Convert to URI

        String url = targetUrl.toString();
        for (int i = 0; i < MAX_PUT_COUNT; i++) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        }
        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
