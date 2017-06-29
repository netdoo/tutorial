package com.esquery;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class BoolQuery {
    final static Logger logger = LoggerFactory.getLogger(BoolQuery.class);

    public static void Sample(TransportClient client) {
        QueryBuilder queryBuilder = boolQuery()
                .must(termQuery("studio", "hollywood"))
                .must(termQuery("title", "superman"))
                .must(termQuery("movie_type", "3d"))
                .mustNot(termQuery("author", "marvel"));

        SearchRequestBuilder builder = client.prepareSearch("bool")
                .setTypes("sample")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", "bool", "sample", builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public static void Sample2(TransportClient client) throws Exception {
        QueryBuilder studio = QueryBuilders.termQuery("studio", "hollywood");
        QueryBuilder title = QueryBuilders.termQuery("title", "superman");
        QueryBuilder movie_type = QueryBuilders.termQuery("movie_type", "3d");
        QueryBuilder author = QueryBuilders.termsQuery("author", Arrays.asList("marvel"));

        QueryBuilder queryBuilder = boolQuery()
                .must(studio)
                .must(title)
                .must(movie_type)
                .mustNot(author);

        SearchRequestBuilder builder = client.prepareSearch("bool")
                .setTypes("sample")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder);

        logger.info("GET {}/{}/_search \n{}", "bool", "sample", builder.toString());
        SearchResponse r = builder.get();
        ObjectMapper om = new ObjectMapper();

        for (SearchHit hit : r.getHits()) {
            String json = hit.getSourceAsString();
            Sample sample = om.readValue(json, Sample.class);
            logger.info("\n\n응답 \n{}", sample.toString());
           // logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }

    public static void Sample3(TransportClient client) throws Exception {
        QueryBuilder studio = QueryBuilders.termQuery("studio", "hollywood");
        QueryBuilder title = QueryBuilders.termQuery("title", "superman");
        QueryBuilder movie_type = QueryBuilders.termQuery("movie_type", "3d");
        QueryBuilder author = QueryBuilders.termsQuery("author", Arrays.asList("marvel"));

        QueryBuilder queryBuilder = boolQuery()
                .must(studio)
                .must(title)
                .must(movie_type)
                .mustNot(author);

        SearchRequestBuilder builder = client.prepareSearch("bool")
                .setTypes("sample")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFetchSource(new String[]{"author","title"}, new String[]{"movie_type"});

        logger.info("GET {}/{}/_search \n{}", "bool", "sample", builder.toString());
        SearchResponse r = builder.get();
        ObjectMapper om = new ObjectMapper();

        for (SearchHit hit : r.getHits()) {
            String json = hit.getSourceAsString();
            Sample sample = om.readValue(json, Sample.class);
            logger.info("\n\n응답 \n{}", sample.toString());
            // logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }
    }
}
