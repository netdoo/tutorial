package com.esquery6;

import com.esquery6.domain.Market;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhkwon78 on 2018-02-01.
 */
public class _6_SearchTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_3_MappingTest.class);
    String indexName = "sample";
    String typeName = "market";
    static TransportClient esClient;
    static ObjectMapper objectMapper;
    static List<Market> markets = getMarkets();

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        //executed only once, before the first test
        esClient = connect();
        List<DiscoveryNode> nodes = esClient.listedNodes();
        nodes.forEach(node -> {
            logger.info("discover node address {}", node.getAddress());
        });

        objectMapper = new ObjectMapper();
    }

    @Test
    public void _01_테스트() throws Exception {

    }
}
