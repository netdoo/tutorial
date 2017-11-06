package com.esjest;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;


public class AppConfig {

    public static final String INDEX = "car";
    public static final String TYPE = "sales";

    public static JestClient create() throws Exception {
        HttpClientConfig clientConfig = new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true).build();
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        return factory.getObject();
    }
}
