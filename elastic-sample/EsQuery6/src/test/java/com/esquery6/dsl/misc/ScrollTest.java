package com.esquery6.dsl.misc;

import com.esquery6.BaseTest;
import com.esquery6.domain.Alphabet;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScrollTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(ScrollTest.class);

    static String indexName = "dummy";
    static String typeName = "alphabet";

    static void initScrollTest() throws Exception {
        // 기존 색인을 삭제하고
        try {
            DeleteIndexResponse deleteIndexResponse = esClient.admin().indices().prepareDelete(indexName).execute().actionGet();

            if (deleteIndexResponse.isAcknowledged() == true) {
                logger.info("delete index {} ", indexName);
            } else {
                logger.error("fail to delete index ");
            }
        } catch (IndexNotFoundException e) {}

        // 샘플 데이터를 입력함.
        try {
            CreateIndexResponse createIndexResponse = esClient.admin().indices().prepareCreate(indexName).execute().actionGet();

            if (createIndexResponse.isAcknowledged() == true) {
                logger.info("create index {} ", indexName);
            } else {
                logger.error("fail to create index ");
            }
        } catch (ResourceAlreadyExistsException e) {
            logger.info("already exists index {} ", indexName);
        }

        // 매핑 생성
        String mappingJson = getResource("ScrollTest.json");

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

        // 테스트용 문서 추가함.
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();

        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (char c : alphabets) {
            Alphabet a = new Alphabet(String.valueOf((c - 'a')), c);

            try {
                bulkRequest.add(esClient.prepareIndex(indexName, typeName)
                        .setId(a.getDocId())
                        .setSource(objectMapper.writeValueAsString(a), XContentType.JSON));
                logger.info("bulk insert request {}", a.getName());
            } catch (Exception e) {
                logger.error("fail to bulk insert request ", e);
            }
        }

        BulkResponse r = bulkRequest.execute().actionGet(5000);

        if (r.hasFailures()) {
            logger.error("fail to bulk insert");
        } else {
            logger.info("bulk insert !!");
        }

        refreshIndex(esClient, indexName, typeName);
    }

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initScrollTest();
        Thread.sleep(3_000);
    }

    @Test
    public void _01_SearchScrollTest() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setScroll(new TimeValue(60000))
                .setQuery(matchAllQuery())
                .addSort(SortBuilders.fieldSort("code").order(SortOrder.ASC))
                .setSize(10);       // 10개씩 반복해서 검색 결과를 조회함. (최대 검색 결과는 10개)

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());
        SearchResponse sr = builder.execute().actionGet();

        String name;
        Integer code;

        do {
            for (SearchHit hit : sr.getHits().getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();
                name = (String)source.getOrDefault("name", "");
                code = (Integer)source.getOrDefault("code", 0);
                logger.info("{} : {}", name, code);
            }

            sr = esClient.prepareSearchScroll(sr.getScrollId()).setScroll(new TimeValue(6, TimeUnit.SECONDS)).execute().actionGet();
        } while(sr.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
    }
}
