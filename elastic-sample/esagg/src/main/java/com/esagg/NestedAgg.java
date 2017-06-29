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
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class NestedAgg {
    final static Logger logger = LoggerFactory.getLogger(NestedAgg.class);

    /*
    GET order/history/_search
    {
        "size" : 0,
            "aggregations" : {
            "menu_agg" : {
                "nested" : {
                    "path" : "menu"
                },
                "aggregations" : {
                    "label_agg" : {
                        "terms" : {
                            "field" : "menu.label"
                        }
                    }
                }
            }
        }
    }
    */
    public void run() throws Exception {
        TransportClient client = AppConfig.create();
        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("menu_agg", "menu")
                .subAggregation(AggregationBuilders.terms("label_agg").field("menu.label"));

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();
        Aggregations aggregations = r.getAggregations();
        InternalNested internalNested = aggregations.get("menu_agg");
        Terms terms = internalNested.getAggregations().get("label_agg");
        Collection<Terms.Bucket> buckets = terms.getBuckets();

        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount() );
        });
    }

    public void run2() throws Exception {
        TransportClient client = AppConfig.create();
        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("menu_agg", "menu")
                .subAggregation(AggregationBuilders.terms("label_agg").field("menu.label"));

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();
        Aggregations aggregations = r.getAggregations();
        InternalNested internalNested = aggregations.get("menu_agg");
        Terms terms = internalNested.getAggregations().get("label_agg");
        Collection<Terms.Bucket> buckets = terms.getBuckets();

        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount() );
        });
    }

    public static void main( String[] args ) throws Exception {
        NestedAgg nestedAgg = new NestedAgg();
       // nestedAgg.run();
        nestedAgg.run2();
    }
}
