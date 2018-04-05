package com.esjest;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BaseTest {

    public static JestClient jestClient = createJestClient();
    public static ObjectMapper objectMapper = createObjectMapper();

    public static JestClient createJestClient() {
        HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        return factory.getObject();
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static String getResource(String name) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BaseTest.class.getResourceAsStream(name)));) {
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return "";
        }
    }
}
