package com.esquery6;

import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.stream.Collectors;

/**
 * Created by jhkwon78 on 2018-01-31.
 */
public class BaseTest {

    public static TransportClient connect(String clusterName, String host, int port) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        return client;
    }

    protected static TransportClient connect() throws Exception {
        return connect(EsConst.clusterName, EsConst.host, EsConst.port);
    }

    public String getResource(String name) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(name)));) {
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return "";
        }
    }

    public RestStatus refreshIndex(TransportClient esClient, String index, String type) throws Exception {

        RefreshResponse response = esClient.admin().indices()
                .prepareRefresh(index)
                .get();

        return response.getStatus();
    }
}
