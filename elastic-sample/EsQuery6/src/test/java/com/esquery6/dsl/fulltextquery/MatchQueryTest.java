package com.esquery6.dsl.fulltextquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

//
//  Full text 쿼리는 보통 이메일 본문과 같은
//  Full text 필드에 대하여 Full text 쿼리를 수행하는데 사용
//
public class MatchQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(MatchQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_MatchAllQuery() throws Exception {
        String include[] = new String[]{"name", "price"};
        String exclude[] = new String[]{"products"};

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFetchSource(include, exclude)
                .setQuery(QueryBuilders.matchAllQuery());

        debugReqRes(builder, logger);
    }

    @Test
    public void _02_Match_문서_검색() throws Exception {

        // 대/소 문자 구분안함.
        // 검색어에 공백이 들어간 경우, OR로 검색됨.
        // 다음과 같은 경우, America 또는 Korea가 포함된 문서중, 최소 1개 이상 매칭되면 검색결과에 포함됨.
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchQuery("country", "America korea").minimumShouldMatch("1"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _03_MatchPhrase_문서_검색() throws Exception {
        // match 쿼리는 용어 사이에 띄어쓰기를 하면 bool-should 쿼리로 처리된다.
        // 띄어쓰기까지 모두 포함해 정확한 구(phrase)를 검색하고 싶다면 match_phrase 쿼리를 사용한다.
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchPhraseQuery("country", "America korea"));

        debugReqRes(builder, logger);
    }

    @Test
    public void _04_MultiMatch_문서_검색() throws Exception {

        // location, country 필드에 korea 또는 china 가 포함된 경우, 검색 결과에 포함됨.
        QueryBuilder qb = QueryBuilders.multiMatchQuery(
                "Korea China",                      // Text you are looking for
                "location", "country"              // Fields you query on
        ).minimumShouldMatch("1");

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _05_CommonTerms_문서_검색() throws Exception {

        // country 필드에 korea 또는 china 가 포함된 경우, 검색 결과에 포함됨.
        QueryBuilder qb = QueryBuilders.commonTermsQuery("country", "the korea a china")
                .cutoffFrequency(0.001f);

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _06_QueryString_문서_검색() throws Exception {

        // country 필드에 korea 또는 china 가 포함된 경우, 검색 결과에 포함됨.
        QueryBuilder qb = QueryBuilders
                            .queryStringQuery("korea OR china")
                            .defaultField("country");

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _07_SimpleQueryString_문서_검색() throws Exception {

        // country 필드에 korea 가 있고, china 가 없는 경우, 검색 결과에 포함됨.
        QueryBuilder qb = QueryBuilders
                .simpleQueryStringQuery("+korea -china")
                .field("country")
                .defaultOperator(Operator.AND);

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }
}
