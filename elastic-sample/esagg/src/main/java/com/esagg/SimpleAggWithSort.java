package com.esagg;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

import java.util.Collection;
public class SimpleAggWithSort {
    final static Logger logger = LoggerFactory.getLogger(SimpleAggWithSort.class);

    public void run() throws Exception {
        TransportClient client = AppConfig.create();
        Terms.Order  order = Terms.Order.term(true);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("order_count").field("guest");

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Aggregations aggregations = r.getAggregations();
        Terms terms = aggregations.get("order_count");
        Collection<Terms.Bucket> buckets = terms.getBuckets();
        List<String> guests = buckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());

        System.out.println("원본 : " + guests);
        Collections.sort(guests, CustomComparatorAsc.TERM_ASC);
        System.out.println("정렬 : " + guests);
    }

    public static void main( String[] args ) throws Exception {
        SimpleAggWithSort simpleAggWithSort = new SimpleAggWithSort();
        simpleAggWithSort.run();
    }
}
