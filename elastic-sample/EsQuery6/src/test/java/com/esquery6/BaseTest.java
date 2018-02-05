package com.esquery6;

import com.esquery6.domain.Market;
import com.esquery6.domain.Product;
import com.esquery6.domain.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseTest {

    public static String indexName = "sample";
    public static String typeName = "market";
    public static TransportClient esClient = connect();
    public static ObjectMapper objectMapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 이곳에 필요한 설정을 추가함.
        return objectMapper;
    }

    public static TransportClient connect(String clusterName, String host, int port) throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        return client;
    }

    protected static TransportClient connect() {

        try {
            return connect(EsConst.clusterName, EsConst.host, EsConst.port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getResource(String name) {
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
        nikeProducts.add(new Product("AirMax Korea", 78900, 6, new Review(4, "i like it", true)));
        nikeProducts.add(new Product("Coretez", 95500, 3, new Review(3, "very preety", true)));
        nikeProducts.add(new Product("White Jordan", 59800, 38, new Review(5, "it is good", true)));

        List<Product> adidasProducts = new ArrayList<>();
        adidasProducts.add(new Product("Duramo", 45500, 10, new Review(4, "i like it", true)));
        adidasProducts.add(new Product("White Cosmic", 50500, 20, new Review(4, "good color", true)));
        adidasProducts.add(new Product("Silly", 70000, 10, new Review(4, "good price", true)));

        List<Product> newBalanceProducts = new ArrayList<>();
        newBalanceProducts.add(new Product("Backpack", 120000, 5, new Review(3, "good", false)));
        newBalanceProducts.add(new Product("White Down", 216700, 1, new Review(5, "nice", true)));
        newBalanceProducts.add(new Product("Shoes", 155980, 8, new Review(3, "very good", false)));

        markets.add(new Market("1", "nike", 50_000, "America", "America", nikeProducts));
        markets.add(new Market("2", "nike", 150_000, "china", "America china", nikeProducts));
        markets.add(new Market("3", "adidas", 20_000,  "America Korea", "America", adidasProducts));
        markets.add(new Market("4", "newbalance", 20_000, "Korea", "china", newBalanceProducts));

        return markets;
    }

    public static void initSearchTest(Logger logger) throws Exception {

        // 기존 색인을 삭제하고
        DeleteIndexResponse deleteIndexResponse = esClient.admin().indices().prepareDelete(indexName).execute().actionGet();

        if (deleteIndexResponse.isAcknowledged() == true) {
            logger.info("delete index {} ", indexName);
        } else {
            logger.error("fail to delete index ");
        }

        // 샘플 데이터를 입력함.
        try {
            CreateIndexResponse createIndexResponse = esClient.admin().indices().prepareCreate(indexName).execute().actionGet();

            if (createIndexResponse.isAcknowledged() == true) {
                logger.info("create index {} ", indexName);
            } else {
                logger.error("fail to create index ");
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.info("already exists index {} ", indexName);
        }


        // 매핑 생성
        String mappingJson = getResource("MappingTest.json");

        PutMappingRequest request = new PutMappingRequest(indexName);
        request.type(typeName);
        request.source(mappingJson, XContentType.JSON);
        request.timeout(TimeValue.timeValueMinutes(2));
        PutMappingResponse putMappingResponse = esClient.admin().indices().putMapping(request).actionGet();

        if (putMappingResponse.isAcknowledged()) {
            logger.info("create mapping");
        } else {
            logger.error("fail to create mapping");
        }

        // 테스트용 문서 추가함.
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();
        List<Market> markets = getMarkets();

        markets.forEach(market -> {
            String json;

            try {
                bulkRequest.add(esClient.prepareIndex(indexName, typeName)
                        .setId(market.getDocId())
                        .setSource(objectMapper.writeValueAsString(market), XContentType.JSON));
                logger.info("bulk insert request {}", market.getName());
            } catch (Exception e) {
                logger.error("fail to bulk insert request ", e);
            }
        });

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk insert");
        } else {
            logger.info("bulk insert !!");
        }

        refreshIndex(esClient, indexName, typeName);
    }

    public static RestStatus refreshIndex(TransportClient esClient, String index, String type) throws Exception {

        RefreshResponse response = esClient.admin().indices()
                .prepareRefresh(index)
                .get();

        return response.getStatus();
    }

    public static void printNodes(Logger logger) {
        List<DiscoveryNode> nodes = esClient.listedNodes();
        nodes.forEach(node -> {
            logger.info("discover node address {}", node.getAddress());
        });
    }

    public static void debugReqRes(SearchRequestBuilder builder, Logger logger) {
        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name, country, location;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();

            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            country = (String)source.getOrDefault("country", "");
            location = (String)source.getOrDefault("location", "");

            logger.info("score : {}, name : {}, price : {}, country : {}, location : {}", hit.getScore(), name, price, country, location);
        }
    }
}
