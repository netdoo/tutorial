package com.esquery6;

import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(IndexTest.class);
    String indexName = "sample";
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
    public void _01_인덱스_생성() throws Exception {
        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(indexName).execute().actionGet();

            if (r.isAcknowledged() == true) {
                logger.info("create index {} ", indexName);
            } else {
                logger.error("fail to create index ");
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.info("already exists index {} ", indexName);
        }
    }

    @Test
    public void _02_인덱스_조회() throws Exception {
        String[] indexList = esClient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getConcreteAllIndices();
        logger.info("index list => {}", indexList);
    }

    @Test
    public void _03_인덱스_삭제() throws Exception {
        DeleteIndexResponse r = esClient.admin().indices().prepareDelete(indexName).execute().actionGet();

        if (r.isAcknowledged() == true) {
            logger.info("delete index {} ", indexName);
        } else {
            logger.error("fail to delete index ");
        }
    }

    @Test
    public void _04_인덱스_조회() throws Exception {
        String[] indexList = esClient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getConcreteAllIndices();
        logger.info("index list => {}", indexList);
    }
}
