package com.esfuncscore;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.weightFactorFunction;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void doRequest(TransportClient client, QueryBuilder queryBuilder) {
        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 스코어 {}\n{}", hit.getScore(), hit.getSourceAsString());
        }
    }

    /// 모든 검색 결과의 score는 5가 됨.
    public static void funcScoreSample(TransportClient client) throws Exception {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {};
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        QueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(matchAllQueryBuilder, functions)
                .boost(5);

        doRequest(client, queryBuilder);
    }

    /// 검색결과에 green 토큰이 포함되어 있는 경우, 스코어에 23을 multiply 해주고,
    /// 검색결과에 black 토큰이 포함되어 있는 경우, 스코어에 50을 multiply 해줌.
    public static void funcWeightSample(TransportClient client) throws Exception {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        termQuery("name", "green"),
                        weightFactorFunction(23)),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        termQuery("name", "black"),
                        weightFactorFunction(50)),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        randomFunction(123)
                )
        };
aaaaaaabbbb
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        QueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(matchAllQueryBuilder, functions)
                .boost(5)
                .boostMode(CombineFunction.MULTIPLY)
                .maxBoost(100);

        doRequest(client, queryBuilder);
    }

    public static void main(String[] args) throws Exception {
        TransportClient client = AppConfig.create();
        funcScoreSample(client);
        funcWeightSample(client);
    }
}
