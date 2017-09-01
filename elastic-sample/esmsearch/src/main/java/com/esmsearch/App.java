package com.esmsearch;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import javax.management.Query;
import static org.elasticsearch.index.query.QueryBuilders.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public static void termSample(TransportClient client) throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "green");
        doRequest(client, queryBuilder);
    }

    /*
    GET cafe/_msearch
    {"index":"cafe"}
    {"query":{"term":{"name":{"value":"black"}}}}
    {"index":"cafe"}
    {"query":{"term":{"name":{"value":"green"}}}}
    */
    public static void msearchSample(TransportClient client) throws Exception {
        SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.termQuery("name", "black"));
        SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.termQuery("name", "green"));

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();

            for (SearchHit hit : response.getHits()) {
                logger.info("\n\n응답 \n{}", hit.getSourceAsString());
            }
        }
    }

    public static void main( String[] args ) throws Exception {
        TransportClient client = AppConfig.create();
        termSample(client);
        msearchSample(client);
    }
}
