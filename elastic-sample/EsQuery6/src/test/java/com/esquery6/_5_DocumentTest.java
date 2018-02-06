package com.esquery6;

import com.esquery6.domain.Market;
import com.esquery6.domain.Product;
import com.esquery6.domain.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _5_DocumentTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(_5_DocumentTest.class);
    static List<Market> markets = getMarkets();

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
    public void _02_매핑_생성() throws Exception {

        String mappingJson = getResource("MappingTest.json");

        PutMappingRequest request = new PutMappingRequest(sampleIndexName);
        request.type(marketTypeName);
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
    public void _03_문서_추가() throws Exception {

        Market market = markets.get(0);
        IndexRequest indexRequest = new IndexRequest(sampleIndexName, marketTypeName, market.getDocId());
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
    public void _04_벌크_문서_추가() throws Exception {
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        markets.forEach(market -> {
            String json;

            try {
                bulkRequest.add(esClient.prepareIndex(sampleIndexName, marketTypeName)
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

        refreshIndex(esClient, sampleIndexName, marketTypeName);
    }

    @Test
    public void _05_문서_조회() throws Exception {

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery());

        SearchResponse response = builder.get();

        response.getHits().forEach(hit -> {
            String json = hit.getSourceAsString();
            logger.info("{}", json);
        });
    }

    @Test
    public void _06_벌크_문서_조회() throws Exception {

        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery(marketTypeName).addIds("1", "2");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(idsQueryBuilder);

        SearchResponse response = builder.get();

        response.getHits().forEach(hit -> {
            String json = hit.getSourceAsString();
            logger.info("{}", json);
        });
    }

    @Test
    public void _07_문서_갱신() throws Exception {
        Market market = markets.get(0);
        market.setPrice(90_000);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(sampleIndexName);
        updateRequest.type(marketTypeName);
        updateRequest.id(market.getDocId());
        updateRequest.doc(objectMapper.writeValueAsString(market), XContentType.JSON);
        UpdateResponse updateResponse = esClient.update(updateRequest).get();
        logger.info("{}", updateResponse.status());
    }


    @Test
    public void _08_벌크_문서_갱신() throws Exception {

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        markets.forEach(market -> {
            String json;

            try {
                market.setPrice(market.getPrice() + 1);

                bulkRequest.add(esClient.prepareUpdate()
                        .setIndex(sampleIndexName)
                        .setType(marketTypeName)
                        .setId(market.getDocId())
                        .setDoc(objectMapper.writeValueAsString(market), XContentType.JSON));
                logger.info("bulk update request {}", market.getName());
            } catch (Exception e) {
                logger.error("fail to bulk update request ", e);
            }
        });

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk update");
        } else {
            logger.info("bulk update !!");
        }
    }

    @Test
    public void _10_문서_삭제() throws Exception {

        _04_벌크_문서_추가();

        Market market = markets.get(0);
        DeleteRequest deleteRequest = new DeleteRequest(sampleIndexName, marketTypeName, market.getDocId());
        DeleteResponse r = esClient.delete(deleteRequest).actionGet();

        if (r.status() == RestStatus.NOT_FOUND) {
            logger.error("fail to delete {}", r.status());
        } else if (r.status() == RestStatus.OK) {
            logger.info("delete docId {} => {}", market.getDocId(), r.status());
        }
    }

    @Test
    public void _11_벌크_문서_삭제() throws Exception {

        _04_벌크_문서_추가();

        logger.info("refresh index {}", refreshIndex(esClient, sampleIndexName, marketTypeName));

        Thread.sleep(2_000);

        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        markets.forEach(market -> {
            try {
                bulkRequest.add(esClient.prepareDelete(sampleIndexName, marketTypeName, market.getDocId()));
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
    public void _12_모든_문서_삭제() throws Exception {

        _04_벌크_문서_추가();

        logger.info("refresh index {}", refreshIndex(esClient, sampleIndexName, marketTypeName));

        Thread.sleep(2_000);

        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient);
        deleteByQueryRequestBuilder.source().setIndices(sampleIndexName).setTypes(marketTypeName);
        BulkByScrollResponse response = deleteByQueryRequestBuilder.filter(QueryBuilders.matchAllQuery()).get();

        long deleted = response.getDeleted();
        logger.info("delete document count {}", deleted);
    }

    @Test
    public void _20_테스트() throws Exception {

    }
}
