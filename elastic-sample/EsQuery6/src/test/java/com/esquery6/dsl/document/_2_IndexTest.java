package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _2_IndexTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(_2_IndexTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
    }

    @Test
    public void _01_인덱스_생성() throws Exception {
        try {
            CreateIndexResponse r = esClient.admin().indices().prepareCreate(sampleIndexName).execute().actionGet();

            if (r.isAcknowledged() == true) {
                logger.info("create index {} ", sampleIndexName);
            } else {
                logger.error("fail to create index ");
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.info("already exists index {} ", sampleIndexName);
        }
    }

    @Test
    public void _02_인덱스_조회() throws Exception {
        String[] indexList = esClient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getConcreteAllIndices();
        logger.info("index list => {}", indexList);
    }

    @Test
    public void _03_인덱스_삭제() throws Exception {
        DeleteIndexResponse r = esClient.admin().indices().prepareDelete(sampleIndexName).execute().actionGet();

        if (r.isAcknowledged() == true) {
            logger.info("delete index {} ", sampleIndexName);
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
