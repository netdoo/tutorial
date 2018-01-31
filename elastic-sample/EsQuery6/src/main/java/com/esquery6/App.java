package com.esquery6;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void doRequest(TransportClient client, QueryBuilder queryBuilder) {
        SearchRequestBuilder builder = client.prepareSearch(EsUtil.INDEX)
                .setTypes(EsUtil.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", EsUtil.INDEX, EsUtil.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    /// black 토큰이 3번째(end) 이전에 위치한 경우 검색하는 쿼리
    public static void spanFirstSample(TransportClient client) throws Exception {
        QueryBuilder queryBuilder = spanFirstQuery(
                spanTermQuery("name", "black"),
                3
        );

        doRequest(client, queryBuilder);
    }

    /// mango 또는 herb 토큰에 대해서 or 검색
    public static void spanOrSample(TransportClient client) throws Exception {
        QueryBuilder queryBuilder = spanOrQuery(
                spanTermQuery("name", "mango")
        ).addClause(
                spanTermQuery("name", "herb")
        );

        doRequest(client, queryBuilder);
    }

    /// magic 토큰과 tea 토큰사이에
    /// 최대 1개(slop)까지의 잘못된 토큰만 허용하여 검색
    public static void spanNearSample(TransportClient client) throws Exception {
        int slop = 1;

        QueryBuilder queryBuilder = spanNearQuery(
                spanTermQuery("name", "magic"),
                slop
        ).addClause(
                spanTermQuery("name", "tea")
        );

        doRequest(client, queryBuilder);
    }

    public static void main(String[] args) throws Exception {
        TransportClient client = EsUtil.connect("localhost", 9300);

        spanFirstSample(client);
        spanOrSample(client);
        spanNearSample(client);
    }
}
