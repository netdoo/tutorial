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
import java.util.*;
import java.util.stream.Collectors;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;

public class NestedSubAgg {

    final static Logger logger = LoggerFactory.getLogger(NestedSubAgg.class);

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
                    },
                    "sum_of_menu_price" : {
                        "sum" : {
                            "field" : "menu.price"
                        }
                    }
                }
            }
        }
    }
    */
    public void run() throws Exception {
        /// 전체 메뉴의 합을 구하는 경우
        TransportClient client = AppConfig.create();
        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("menu_agg", "menu");
        aggregationBuilder.subAggregation(AggregationBuilders.terms("label_agg").field("menu.label"));
        aggregationBuilder.subAggregation(AggregationBuilders.sum("sum_of_menu_price").field("menu.price"));

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

        InternalSum sum = internalNested.getAggregations().get("sum_of_menu_price");
        System.out.println("sum : " + sum.getValue());

        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount());
        });
    }

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
                        },
                        "aggregations" : {
                            "sum_of_menu_price" : {
                                "sum" : {
                                    "field" : "menu.price"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    */
    public void run2() throws Exception {
        /// 각 메뉴별 합을 구하는 경우
        TransportClient client = AppConfig.create();
        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("menu_agg", "menu");
        aggregationBuilder.subAggregation(
                AggregationBuilders.terms("label_agg").field("menu.label")
                        .subAggregation(AggregationBuilders.sum("sum_of_menu_price").field("menu.price"))
        );

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
            InternalSum sum = bucket.getAggregations().get("sum_of_menu_price");
            System.out.println(bucket.getKeyAsString() + ":"  + bucket.getDocCount() + ", sum : " + sum.getValue());
        });
    }

    public static void main( String[] args ) throws Exception {
        NestedSubAgg nestedSubAgg = new NestedSubAgg();
        //nestedSubAgg.run();
        nestedSubAgg.run2();
    }
}
