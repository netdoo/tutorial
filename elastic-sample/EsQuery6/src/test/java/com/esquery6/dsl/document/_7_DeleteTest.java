package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class _7_DeleteTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_7_DeleteTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_DeleteTest() throws Exception {
        String id = "1";
        DeleteResponse response = esClient.prepareDelete(sampleIndexName, marketTypeName, id).get();

        if (response.status() == RestStatus.OK) {
            logger.info("delete doc id {}", id);
        } else if (response.status() == RestStatus.NOT_FOUND) {
            logger.info("no such doc id {}", id);
        } else {
            logger.error("fail to delete doc id {}", id);
        }
    }

    @Test
    public void _02_DeleteByQueryTest() throws Exception {
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient);
        deleteByQueryRequestBuilder.source().setIndices(sampleIndexName).setTypes(marketTypeName);
        BulkByScrollResponse response = deleteByQueryRequestBuilder.filter(matchQuery("name", "nike")).get();

        long deleted = response.getDeleted();
        logger.info("delete document count {}", deleted);
    }
}
