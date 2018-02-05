package com.esquery6.dsl.joiningquery;

import com.esquery6.BaseTest;
import com.esquery6.dsl.termquery.TermQueryTest;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class JoiningQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(TermQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_NestedQuery_테스트() throws Exception {

        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
            "products",
            matchQuery("products.label", "Airmax"),
            ScoreMode.Avg
        );

        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        debugReqRes(builder, logger);
    }

    @Test
    public void _02_NestedQuery_테스트() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
                "products.review",
                rangeQuery("products.review.star").gte(5),
                ScoreMode.Avg
        );

        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        debugReqRes(builder, logger);
    }

    @Test
    public void _03_NestedQuery_테스트() throws Exception {
        QueryBuilder subQueryBuilder = QueryBuilders.nestedQuery(
                "products.review",
                matchQuery("products.review.message", "good"),
                ScoreMode.Avg
        );

        // products.label 에 white 가 포함되어 있는 결과들중,
        // products.review.message 에 good 이 포함되어 있으면 검색 결과에 포함됨.
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(
                "products",
                boolQuery()
                .must(matchQuery("products.label", "white"))
                .must(subQueryBuilder),
                ScoreMode.Avg
        );

        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        debugReqRes(builder, logger);
    }
}
