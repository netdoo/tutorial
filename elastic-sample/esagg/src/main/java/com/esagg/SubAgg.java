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
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;

public class SubAgg {
    final static Logger logger = LoggerFactory.getLogger(SubAgg.class);

    /*
    GET order/history/_search
    {
        "size": 0,
            "aggs": {
            "guest_orders": {
                "terms": {
                    "field": "guest"
                },
                "aggs": {
                    "sum_of_payment": {
                        "sum": {
                            "field": "payment"
                        }
                    }
                }
            }
        }
    }
    */
    public void run() throws Exception {

        /// guest 필드를 기준으로 집계하고(group by guest), payment 필드는 sum 함수를 적용하여 집계 (sum(payment))
        TransportClient client = AppConfig.create();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("guest_orders").field("guest")
                .subAggregation(AggregationBuilders.sum("sum_of_payment").field("payment"));

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        Aggregations aggregations = r.getAggregations();
        Terms terms = aggregations.get("guest_orders");
        Collection<Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
            InternalSum sum = bucket.getAggregations().get("sum_of_payment");
            System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount() + ", sum_of_payment : " + sum.getValue());
        });
    }

    public static void main( String[] args ) throws Exception {
        SubAgg subAgg = new SubAgg();
        subAgg.run();
    }
}
