package com.esquery6;

import com.esquery6.domain.Market;
import com.esquery6.domain.Product;
import com.esquery6.domain.Review;
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
import java.util.ArrayList;
import java.util.List;
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

    public static List<Market> getMarkets() {
        List<Market> markets = new ArrayList<>();

        List<Product> nikeProducts = new ArrayList<>();
        nikeProducts.add(new Product("AirMax", 78900, 6, new Review(4, "만족", true)));
        nikeProducts.add(new Product("Coretez", 95500, 3, new Review(3, "이뻐요", true)));
        nikeProducts.add(new Product("Jordan", 59800, 38, new Review(5, "좋습니다", true)));

        List<Product> adidasProducts = new ArrayList<>();
        adidasProducts.add(new Product("Duramo", 45500, 10, new Review(4, "편해요", true)));
        adidasProducts.add(new Product("Cosmic", 50500, 20, new Review(4, "가벼워요", true)));
        adidasProducts.add(new Product("Silly", 70000, 10, new Review(4, "만족", true)));

        List<Product> newBalanceProducts = new ArrayList<>();
        newBalanceProducts.add(new Product("Backpack", 120000, 5, new Review(3, "무거워요", false)));
        newBalanceProducts.add(new Product("Down", 216700, 1, new Review(5, "따뜻해요", true)));
        newBalanceProducts.add(new Product("Shoes", 155980, 8, new Review(3, "무거워요", false)));

        markets.add(new Market("1", "nike", 50_000, nikeProducts));
        markets.add(new Market("2", "adidas", 20_000, adidasProducts));
        markets.add(new Market("3", "newbalance", 20_000, newBalanceProducts));

        return markets;
    }

    public RestStatus refreshIndex(TransportClient esClient, String index, String type) throws Exception {

        RefreshResponse response = esClient.admin().indices()
                .prepareRefresh(index)
                .get();

        return response.getStatus();
    }


}
