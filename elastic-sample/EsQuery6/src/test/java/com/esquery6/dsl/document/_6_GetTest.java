package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _6_GetTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_6_GetTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    void printResponse(GetResponse response) {
        String name, country, location;
        Integer price;

        Map<String, Object> source = response.getSourceAsMap();

        name = (String)source.getOrDefault("name", "");
        price = (Integer)source.getOrDefault("price", 0);
        country = (String)source.getOrDefault("country", "");
        location = (String)source.getOrDefault("location", "");

        logger.info("id : {}, name : {}, price : {}, country : {}, location : {}", response.getId(), name, price, country, location);
    }

    @Test
    public void _01_GetTest() throws Exception {
        GetResponse response = esClient.prepareGet(sampleIndexName, marketTypeName, "1").get();
        printResponse(response);
    }

    @Test
    public void _02_MultiGetTest() throws Exception {
        MultiGetResponse multiGetItemResponses = esClient.prepareMultiGet()
                .add(sampleIndexName, marketTypeName, "1")
                .add(sampleIndexName, marketTypeName, "2")
                .add(sampleIndexName, marketTypeName, "3")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                printResponse(response);
            } else {
                logger.error("response is not exist");
            }
        }
    }
}
