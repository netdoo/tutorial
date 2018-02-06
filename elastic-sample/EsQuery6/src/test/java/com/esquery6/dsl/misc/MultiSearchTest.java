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
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.search.SearchHit;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultiSearchTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(MultiSearchTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_MultiSearchTest() throws Exception {
        SearchRequestBuilder builder1 = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchQuery("name", "nike").minimumShouldMatch("1"));

        SearchRequestBuilder builder2 = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchQuery("name", "adidas").minimumShouldMatch("1"));

        MultiSearchResponse sr = esClient.prepareMultiSearch()
                .add(builder1)
                .add(builder2)
                .get();

        String name;
        Integer price;

        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            long searchCount = response.getHits().getTotalHits();

            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();
                name = (String)source.getOrDefault("name", "");
                price = (Integer)source.getOrDefault("price", 0);
                logger.info("score : {}, id : {}, name : {}, price : {}", hit.getScore(), hit.getId(), name, price);
            }
        }
    }
}
