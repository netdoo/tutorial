package com.esmsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class AppConfig {
    public static final String INDEX = "cafe";
    public static final String TYPE = "menu";

    public static TransportClient create() throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", "my-elastic").build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

        return client;
    }
}
