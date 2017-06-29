package com.esquery;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    TransportClient client;

    public App() throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public void close() {
        client.close();
        logger.info("close client");
    }

    public TransportClient getClient() {
        return client;
    }

    public static void main( String[] args ) throws Exception {
        App app = new App();
        //BoolQuery.Sample(app.getClient());
        System.out.println("=======================================");
        BoolQuery.Sample3(app.getClient());
        app.close();
    }
}
