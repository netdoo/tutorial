package com.esquery6;

import com.esquery6.domain.Market;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.error.Mark;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(DocumentTest.class);
    String indexName = "sample";
    String typeName = "market";
    static TransportClient esClient;
    static ObjectMapper objectMapper;
    static List<Market> markets;

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        //executed only once, before the first test
        esClient = connect();

        List<DiscoveryNode> nodes = esClient.listedNodes();
        nodes.forEach(node -> {
            logger.info("discover node address {}", node.getAddress());
        });

        objectMapper = new ObjectMapper();

        markets = new ArrayList<>();
        markets.add(new Market("1", "nike", 50_000));
        markets.add(new Market("2", "lotte", 20_000));
        markets.add(new Market("3", "ebs", 20_000));
    }

    @Test
    public void _1_인덱스_생성() throws Exception {
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
    public void _2_매핑_생성() throws Exception {

        String mappingJson = getResource("MappingTest.txt");

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
    }

    @Test
    public void _3_문서_추가() throws Exception {

        Market market = markets.get(0);
        IndexRequest indexRequest = new IndexRequest(indexName, typeName, market.getDocId());
        String json = objectMapper.writeValueAsString(market);

        indexRequest.source(json, XContentType.JSON);
        IndexResponse r = esClient.index(indexRequest).actionGet();

        if (r.status() == RestStatus.CREATED) {
            logger.info("create doc");
        } else if (r.status() == RestStatus.OK) {
            logger.info("update doc");
        } else {
            logger.error("fail to create doc {}", r.status());
        }
    }

    @Test
    public void _4_벌크_문서_추가() throws Exception {
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

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
    }

    @Test
    public void _5_문서_삭제() throws Exception {

        _4_벌크_문서_추가();

        Market market = markets.get(0);
        DeleteRequest deleteRequest = new DeleteRequest(indexName, typeName, market.getDocId());
        DeleteResponse r = esClient.delete(deleteRequest).actionGet();

        if (r.status() == RestStatus.NOT_FOUND) {
            logger.error("fail to delete {}", r.status());
        } else if (r.status() == RestStatus.OK) {
            logger.info("delete docId {} => {}", market.getDocId(), r.status());
        }
    }

    @Test
    public void _5_벌크_문서_삭제() throws Exception {

        _4_벌크_문서_추가();

        logger.info("refresh index {}", refreshIndex(esClient, indexName, typeName));

        Thread.sleep(2_000);

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        markets.forEach(market -> {
            try {
                bulkRequest.add(esClient.prepareDelete(indexName, typeName, market.getDocId()));
                logger.info("bulk delete request {}", market.getName());
            } catch (Exception e) {
                logger.error("fail to bulk delete request ", e);
            }
        });

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk delete");
        } else {
            logger.info("bulk delete !!");
        }
    }

    @Test
    public void _6_모든_문서_삭제() throws Exception {

        _4_벌크_문서_추가();

        logger.info("refresh index {}", refreshIndex(esClient, indexName, typeName));

        Thread.sleep(2_000);

        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient);
        deleteByQueryRequestBuilder.source().setIndices(indexName).setTypes(typeName);
        BulkByScrollResponse response = deleteByQueryRequestBuilder.filter(QueryBuilders.matchAllQuery()).get();

        long deleted = response.getDeleted();
        logger.info("delete document count {}", deleted);
    }
}
