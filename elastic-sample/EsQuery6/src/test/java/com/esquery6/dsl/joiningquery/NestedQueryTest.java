package com.esquery6.dsl.joiningquery;

import com.esquery6.BaseTest;
import com.esquery6.domain.Author;
import com.esquery6.domain.BookStoreDoc;
import com.esquery6.domain.Market;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NestedQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(NestedQueryTest.class);
    final static String indexName = "bookstore";
    final static String typeName = "doc";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        printNodes(logger);


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
        String mappingJson = getResource("NestedTest.json");

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
        List<BookStoreDoc> bookStoreDocs = new ArrayList<>();
        bookStoreDocs.add(new BookStoreDoc("1", 1L, Arrays.asList(new Author("James", 22), new Author("Jane", 12))));

        bookStoreDocs.forEach(bookStoreDoc -> {
            String json;

            try {
                bulkRequest.add(esClient.prepareIndex(indexName, typeName)
                        .setId(bookStoreDoc.getDocId())
                        .setSource(objectMapper.writeValueAsString(bookStoreDoc), XContentType.JSON));
                logger.info("bulk insert request {}", bookStoreDoc);
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

        refreshIndex(esClient, indexName, typeName);
    }

    @Test
    public void _01_NestedQuery_테스트() throws Exception {

        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
                "author",
                matchQuery("author.name", "James"),
                ScoreMode.Avg
        );

        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        debugReqRes(builder, logger);
    }

}
