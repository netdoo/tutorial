package com.esquery6.dsl.compoundquery;

import com.esquery6.dsl.termquery.TermQueryTest;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.esquery6.BaseTest.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.weightFactorFunction;

public class CompoundQueryTest extends Serializers.Base {
    final static Logger logger = LoggerFactory.getLogger(CompoundQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_ConstantScoreQuery_문서_검색() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(constantScoreQuery(termQuery("name","nike")).boost(2.0f));

        debugReqRes(builder, logger);
    }

    @Test
    public void _02_Bool_문서_검색() throws Exception {
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
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _03_DisMax_문서_검색() throws Exception {
        // This is useful when searching for a word in multiple fields with different boost factors
        DisMaxQueryBuilder qb = new DisMaxQueryBuilder()
                .add(termQuery("name", "nike").boost(2.0f))
                .add(termQuery("name", "adidas").boost(1.0f));

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }

    @Test
    public void _04_FuncScore_문서_검색() throws Exception {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        termQuery("name", "nike"),
                        weightFactorFunction(23)),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        termQuery("name", "adidas"),
                        weightFactorFunction(50)),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        randomFunction().seed(123)
                )
        };

        QueryBuilder qb = QueryBuilders.functionScoreQuery(matchAllQuery(), functions)
                .boost(5)
                .boostMode(CombineFunction.MULTIPLY)
                .maxBoost(100);

        SearchRequestBuilder builder = esClient.prepareSearch()
                .setIndices(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb);

        debugReqRes(builder, logger);
    }
}
