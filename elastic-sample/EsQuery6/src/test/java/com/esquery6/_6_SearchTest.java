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

public class _6_SearchTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_3_MappingTest.class);
    static List<Market> markets = getMarkets();

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
    }

    @Test
    public void _01_테스트() throws Exception {

    }
}
