package com.esquery6.dsl.termquery;

import com.esquery6.BaseTest;
import com.esquery6._3_MappingTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

/*
While the full text queries will analyze the query string before executing,
the term-level queries operate on the exact terms that are stored in the inverted index.

These queries are usually used for structured data like numbers, dates, and enums,
rather than full text fields. Alternatively, they allow you to craft low-level queries, foregoing the analysis process.
*/
public class TermQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(TermQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_Term_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("name", "nike"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _02_Terms_문서_검색() throws Exception {
        // name 필드에 값이 nike 또는 adidas 인 경우
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termsQuery("name", "nike", "adidas"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _03_RangeQuery_문서_검색() throws Exception {
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

        debugReqRes(builder, logger);
    }

    @Test
    public void _04_ExistQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(existsQuery("name"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _05_PrefixQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(prefixQuery("name", "nik"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _06_WildcardQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(wildcardQuery("name", "nik*"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _07_Regexp_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(regexpQuery("name", "ni.*e"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _08_Fuzzy_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(fuzzyQuery("name", "adid").fuzziness(Fuzziness.TWO));

        debugReqRes(builder, logger);
    }

    @Test
    public void _09_Ids_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(idsQuery().addIds("1", "2", "3"));

        debugReqRes(builder, logger);
    }
}
