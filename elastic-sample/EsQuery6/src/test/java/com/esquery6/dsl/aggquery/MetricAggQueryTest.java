package com.esquery6.dsl.aggquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.fielddata.IndexFieldData;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class MetricAggQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(MetricAggQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_TermAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("product_count").field("name");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse r = builder.get();
        Aggregations aggregations = r.getAggregations();

        Terms terms = aggregations.get("product_count");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();

        buckets.forEach(bucket -> {
            logger.info("{} : {} ", bucket.getKeyAsString(), bucket.getDocCount());
        });
    }

    @Test
    public void _02_SumAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.sum("sum_of_price").field("price");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        // sr is here your SearchResponse object
        Aggregations aggregation = sr.getAggregations();
        Sum agg = aggregation.get("sum_of_price");
        double value = agg.getValue();
        logger.info("sum {}", value);
    }

    @Test
    public void _03_MinAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.min("min_of_price").field("price");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        // sr is here your SearchResponse object
        Aggregations aggregation = sr.getAggregations();
        Min agg = aggregation.get("min_of_price");
        double value = agg.getValue();
        logger.info("min {}", value);
    }

    @Test
    public void _04_MaxAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.max("max_of_price").field("price");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        // sr is here your SearchResponse object
        Aggregations aggregation = sr.getAggregations();
        Max agg = aggregation.get("max_of_price");
        double value = agg.getValue();
        logger.info("max {}", value);
    }

    @Test
    public void _05_AvgAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.avg("avg_of_price").field("price");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        // sr is here your SearchResponse object
        Aggregations aggregation = sr.getAggregations();
        Avg agg = aggregation.get("avg_of_price");
        double value = agg.getValue();
        logger.info("avg {}", value);
    }

    @Test
    public void _06_CountAggQueryTest() throws Exception {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        AggregationBuilder aggregationBuilder = AggregationBuilders.count("count_of_price").field("price");

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        // sr is here your SearchResponse object
        Aggregations aggregation = sr.getAggregations();
        ValueCount agg = aggregation.get("count_of_price");
        long value = agg.getValue();
        logger.info("count {}", value);
    }

    @Test
    public void _07_TermFilterAggQueryTest() throws Exception {
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .filters("agg",
                new FiltersAggregator.KeyedFilter("nike", matchQuery("name", "nike") ),
                new FiltersAggregator.KeyedFilter("adidas", matchQuery("name", "nike")));

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchAllQuery())
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();
        Aggregations aggregations = sr.getAggregations();
        Filters filters = aggregations.get("agg");

        List<? extends Filters.Bucket> buckets = filters.getBuckets();

        buckets.forEach(bucket -> {
            logger.info("{} : {} ", bucket.getKeyAsString(), bucket.getDocCount());
        });
    }

    @Test
    public void _08_NestedAggQueryTest() throws Exception {
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .nested("agg", "products")
                .subAggregation(AggregationBuilders
                    .terms("name").field("products.label")
                );

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchAllQuery())
                .addAggregation(aggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();
        Aggregations aggregations = sr.getAggregations();

        Nested agg = aggregations.get("agg");
        Terms name = agg.getAggregations().get("name");

        for (Terms.Bucket bucket : name.getBuckets()) {
            logger.info("{} : {} ", bucket.getKeyAsString(), bucket.getDocCount());
        }
    }
}
