package com.esquery6;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class _7_SearchAggTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_7_SearchAggTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
    }

    @Test
    public void _01_테스트() throws Exception {

    }
}
