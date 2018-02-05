package com.esquery6;

import com.esquery6.domain.Market;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/*
        쿼리를 사용하기 위해서는 색인되어 있어야 함. (index : true)

        "name": {
            "type": "keyword",
            "index": true
        },
*/
public class _6_SearchTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_3_MappingTest.class);
    static List<Market> markets = getMarkets();

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        /*
        printNodes(logger);

        // 기존 색인을 삭제하고
        _2_IndexTest indexTest = new _2_IndexTest();
        indexTest._03_인덱스_삭제();

        // 샘플 데이터를 입력함.
        _5_DocumentTest documentTest = new _5_DocumentTest();
        documentTest._01_인덱스_생성();
        documentTest._02_매핑_생성();
        documentTest._04_벌크_문서_추가();
        */
    }



    @Test
    public void _02_Term_문서_검색() throws Exception {

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("name", "nike"));

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }


    @Test
    public void _04_Regexp_문서_검색() throws Exception {

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(regexpQuery("name", "ni.*e"));

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }



    @Test
    public void _06_Ids_문서_검색() throws Exception {

        QueryBuilder qb = QueryBuilders.idsQuery().addIds("1", "2", "3");

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }

    @Test
    public void _07_ConstantScoreQuery_문서_검색() throws Exception {

        QueryBuilder qb = QueryBuilders.constantScoreQuery(termQuery("name","nike"))
                .boost(2.0f);

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }

    @Test
    public void _08_PrefixQuery_문서_검색() throws Exception {

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(prefixQuery("name", "nik"));

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }

    @Test
    public void _09_RangeQuery_문서_검색() throws Exception {

        QueryBuilder qb = QueryBuilders
                .rangeQuery("price")
                .from(20_000)
                .to(50_000)
                .includeLower(true)         // lower 조건을 체크함.
                .includeUpper(false);       // upper 조건을 무시함. (즉, 50_000 이란 값이 무시됨.)

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }

    @Test
    public void _10_Bool_문서_검색() throws Exception {
/*
        must     : 반드시 포함
        must not : 반드시 불포함
        should   : OR 조건
*/

        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(rangeQuery("price").from(10_000).to(50_000))
                .mustNot(termQuery("name", "newbalance"))
                .should(termQuery("products.label", "jordan"));

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
    }

    @Test
    public void _11_Terms_문서_검색() throws Exception {

        /*
        QueryBuilder qb = QueryBuilders.termsQuery("name",    // field
                "nike", "adidas")               // values
                .minimumMatch(1);              // How many terms must match


        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(rangeQuery("price").from(10_000).to(50_000))
                .mustNot(termQuery("name", "newbalance"))
                .should(termQuery("products.label", "jordan"));

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        logger.info("Request Query\nGET {}/{}/_search\n{}", indexName, typeName, builder.toString());

        SearchResponse r = builder.execute().actionGet();

        String name;
        Integer price;

        long searchCount = r.getHits().getTotalHits();

        for (SearchHit hit : r.getHits()) {

            Map<String, Object> source = hit.getSourceAsMap();
            source.forEach((key, value) -> {
                // logger.info("key : {}, value : {}", key, value);
            });

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            logger.info("name : {}, price : {}", name, price);
        }
        */
    }

}
