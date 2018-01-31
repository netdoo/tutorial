package com.esquery6;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class EsUtil {

    public static TransportClient connect(String clusterName, String host, int port) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        return client;
    }
}
