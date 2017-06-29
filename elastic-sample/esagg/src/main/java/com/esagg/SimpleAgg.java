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

import java.util.Collection;

public class SimpleAgg {
    final static Logger logger = LoggerFactory.getLogger(SimpleAgg.class);

    /*
    GET order/history/_search
    {
        "size" : 0,
        "query" : {
            "match_all" : {
                "boost" : 1.0
            }
        },
            "aggregations" : {
            "order_count" : {
                "terms" : {
                    "field" : "guest"
                }
            }
        }
    }
    */
    public void run() throws Exception {
        TransportClient client = AppConfig.create();
        Terms.Order  order = Terms.Order.term(true);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("order_count").field("guest");

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Aggregations aggregations = r.getAggregations();
        Terms terms = aggregations.get("order_count");
        Collection<Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
    }

    public void run2() throws Exception {
        TransportClient client = AppConfig.create();
        Terms.Order  order = Terms.Order.term(true);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggGuestOrderCount = AggregationBuilders.terms("order_count").field("guest");
        AggregationBuilder aggGradeOrderCount = AggregationBuilders.terms("grade_count").field("grade");

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggGuestOrderCount)
                .addAggregation(aggGradeOrderCount)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Aggregations aggregations = r.getAggregations();
        Terms gradeCountTerms = aggregations.get("grade_count");
        Collection<Terms.Bucket> gradeCountTermsBuckets = gradeCountTerms.getBuckets();
        gradeCountTermsBuckets.forEach(bucket -> System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));

        System.out.println("==========================================");

        Terms orderCountTerms = aggregations.get("order_count");
        Collection<Terms.Bucket> orderCountTermsBuckets = orderCountTerms.getBuckets();
        orderCountTermsBuckets.forEach(bucket -> System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
    }

    public static void main( String[] args ) throws Exception {
        SimpleAgg simpleAgg = new SimpleAgg();
        //simpleAgg.run();
        simpleAgg.run2();
    }
}
