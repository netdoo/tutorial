package com.esquery6.dsl.termquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _2_RangeQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(_2_RangeQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _02_DateRangeQueryTest() throws Exception {
        QueryBuilder qb = QueryBuilders
                .rangeQuery("createDate")
                .from("2015-01-01 00:00:00")
                .to( "2015-01-05 00:00:00")
                .includeLower(true)         // lower 조건을 체크함.
                .includeUpper(false);       // upper 조건을 무시함. (즉, 50_000 이란 값이 무시됨.)

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _03_NumericRangeQueryTest() throws Exception {
        QueryBuilder qb = QueryBuilders
                .rangeQuery("price")
                .from(20_000)
                .to(50_000)
                .includeLower(true)         // lower 조건을 체크함.
                .includeUpper(false);       // upper 조건을 무시함. (즉, 50_000 이란 값이 무시됨.)

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }
}
