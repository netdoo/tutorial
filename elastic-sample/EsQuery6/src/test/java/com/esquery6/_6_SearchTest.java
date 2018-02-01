package com.esquery6;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jhkwon78 on 2018-02-01.
 */
public class _6_SearchTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_3_MappingTest.class);
    String indexName = "sample";
    String typeName = "market";
    static TransportClient esClient;

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        //executed only once, before the first test
        esClient = connect();
        List<DiscoveryNode> nodes = esClient.listedNodes();
        nodes.forEach(node -> {
            logger.info("discover node address {}", node.getAddress());
        });
    }

    @Test
    public void _01_테스트() throws Exception {

    }
}
