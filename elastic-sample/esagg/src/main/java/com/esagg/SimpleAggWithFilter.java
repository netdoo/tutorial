package com.esagg;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class SimpleAggWithFilter {

    final static Logger logger = LoggerFactory.getLogger(SimpleAggWithFilter.class);
    /**
    GET order/history/_search
    {
        "size": 0,
        "aggs" : {
            "aggs_guest_filter" : {
                "filter" : {
                    "term": {
                        "guest": "james"
                    }
                },
                "aggs" : {
                    "aggs_guest" : {
                        "terms" : {
                            "field" : "guest"
                        }
                    }
                }
            }
        }
    }
    */
    /// filter agg
    public void run() throws Exception {
        TransportClient client = AppConfig.create();

        AggregationBuilder aggregationBuilder = AggregationBuilders.filter("aggs_guest_filter", QueryBuilders.termQuery("guest", "james"))
                .subAggregation(AggregationBuilders.terms("aggs_guest").field("guest"));

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Filter guestFilter = r.getAggregations().get("aggs_guest_filter");
        Terms terms = guestFilter.getAggregations().get("aggs_guest");
        logger.info("filter name : {}, doc count : {}", guestFilter.getName(), guestFilter.getDocCount());
        Collection<Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> logger.info(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
    }

    /// filters sub agg
    public void run2() throws Exception {
        TransportClient client = AppConfig.create();

        AggregationBuilder aggregationBuilder = AggregationBuilders.filters("aggs_guest_filters",
                    new FiltersAggregator.KeyedFilter("james_filter", QueryBuilders.termQuery("guest", "james")),
                    new FiltersAggregator.KeyedFilter("namsu_filter", QueryBuilders.termQuery("guest", "namsu"))
                )
                .subAggregation(AggregationBuilders.terms("aggs_guest").field("guest"));

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Filters guestFilters = r.getAggregations().get("aggs_guest_filters");

        for (Filters.Bucket entry : guestFilters.getBuckets()) {
            logger.info("key : {}, doc count : {}", entry.getKeyAsString(), entry.getDocCount());
            Terms terms = entry.getAggregations().get("aggs_guest");
            Collection<Terms.Bucket> buckets = terms.getBuckets();
            buckets.forEach(bucket -> logger.info(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
        }
    }

    /// nested sub agg + filter
    public void run3() throws Exception {
        TransportClient client = AppConfig.create();

        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("aggs_nested_menu", "menu")
                .subAggregation(
                    AggregationBuilders.filter("aggs_menu_label_filter", QueryBuilders.termQuery("menu.label", "pizza"))
                    .subAggregation(AggregationBuilders.terms("aggs_menu_label").field("menu.label"))
                );

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Nested nestedMenu = r.getAggregations().get("aggs_nested_menu");
        Filter menuLabelFilter = nestedMenu.getAggregations().get("aggs_menu_label_filter");

        Terms terms = menuLabelFilter.getAggregations().get("aggs_menu_label");
        Collection<Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> logger.info(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
    }

    /// nested sub agg + filters + sub agg
    public void run4() throws Exception {
        TransportClient client = AppConfig.create();

        AggregationBuilder aggregationBuilder = AggregationBuilders.nested("aggs_nested_menu", "menu")
                .subAggregation(
                        ////
                        AggregationBuilders.filters("aggs_menu_label_filters",
                                new FiltersAggregator.KeyedFilter("pizza_filter", QueryBuilders.termQuery("menu.label", "pizza")),
                                new FiltersAggregator.KeyedFilter("coke_filter", QueryBuilders.termQuery("menu.label", "coke"))
                        )
                        .subAggregation(AggregationBuilders.terms("agg_menu_label").field("menu.label"))
                );

        SearchRequestBuilder builder = client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(aggregationBuilder)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", AppConfig.INDEX, AppConfig.TYPE, builder.toString());
        SearchResponse r = builder.get();

        for (SearchHit hit : r.getHits()) {
            logger.info("\n\n응답 \n{}", hit.getSourceAsString());
        }

        Nested nestedMenu = r.getAggregations().get("aggs_nested_menu");
        Filters menuLabelFilters = nestedMenu.getAggregations().get("aggs_menu_label_filters");

        for (Filters.Bucket entry : menuLabelFilters.getBuckets()) {
            logger.info("key : {}, doc_count : {}", entry.getKeyAsString(), entry.getDocCount());
            Terms menuLabels = entry.getAggregations().get("agg_menu_label");
            Collection<Terms.Bucket> buckets = menuLabels.getBuckets();
            buckets.forEach(bucket -> logger.info(bucket.getKeyAsString() + ":"  + bucket.getDocCount()));
        }
    }

    public static void main( String[] args ) throws Exception {
        SimpleAggWithFilter app = new SimpleAggWithFilter();
        app.run();
        app.run2();
        app.run3();
        app.run4();
    }
}
