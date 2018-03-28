package com.exgooglehttpclient;

import com.exgooglehttpclient.model.GitHubUrl;
import com.exgooglehttpclient.model.User;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.gson.reflect.TypeToken;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void _01_SimpleRequestTest() throws Exception {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl("https://github.com"));
        String rawResponse = request.execute().parseAsString();
        logger.info("{}", rawResponse);
    }

    @Test
    public void _02_SimpleJsonRequestTest() throws Exception {
        HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new JacksonFactory();

        HttpRequestFactory requestFactory
                = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        GitHubUrl url = new GitHubUrl("https://api.github.com/users");
        url.per_page = 10;
        HttpRequest request = requestFactory.buildGetRequest(url);
        Type type = new TypeToken<List<User>>() {}.getType();
        List<User> users = (List<User>)request
                .execute()
                .parseAs(type);

        logger.info("{}", users);
    }

    @Test
    public void _03_RetryTest() throws Exception {

        HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new JacksonFactory();

        HttpRequestFactory requestFactory
                = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });

        GitHubUrl url = new GitHubUrl("https://api.github.com/users");
        url.per_page = 10;
        HttpRequest request = requestFactory.buildGetRequest(url);
        ExponentialBackOff backoff = new ExponentialBackOff.Builder()
                .setInitialIntervalMillis(500)
                .setMaxElapsedTimeMillis(900000)
                .setMaxIntervalMillis(6000)
                .setMultiplier(1.5)
                .setRandomizationFactor(0.5)
                .build();
        request.setUnsuccessfulResponseHandler(
                new HttpBackOffUnsuccessfulResponseHandler(backoff));

        Type type = new TypeToken<List<User>>() {}.getType();
        List<User> users = (List<User>)request
                .execute()
                .parseAs(type);

        logger.info("{}", users);

    }
}
