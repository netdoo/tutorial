package com.esquery6.dsl.termquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.*;

/*
While the full text queries will analyze the query string before executing,
the term-level queries operate on the exact terms that are stored in the inverted index.

These queries are usually used for structured data like numbers, dates, and enums,
rather than full text fields. Alternatively, they allow you to craft low-level queries, foregoing the analysis process.
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _1_TermQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_1_TermQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_Term_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("name", "nike"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _02_Terms_문서_검색() throws Exception {
        // name 필드에 값이 nike 또는 adidas 인 경우
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termsQuery("name", "nike", "adidas"));

        debugReqRes(builder, logger);
    }


    @Test
    public void _04_ExistQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(existsQuery("name"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _05_PrefixQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(prefixQuery("name", "nik"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _06_WildcardQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(wildcardQuery("name", "nik*"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _07_Regexp_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(regexpQuery("name", "ni.*e"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _08_Fuzzy_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(fuzzyQuery("name", "adid").fuzziness(Fuzziness.TWO));

        debugReqRes(builder, logger);
    }

    @Test
    public void _09_Ids_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(idsQuery().addIds("1", "2", "3"));

        debugReqRes(builder, logger);
    }
}
