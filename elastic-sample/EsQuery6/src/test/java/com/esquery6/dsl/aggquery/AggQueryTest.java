package com.esquery6.dsl.aggquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AggQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(AggQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_MultiAggTest() throws Exception {
        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchAllQuery())
                .addAggregation(AggregationBuilders.sum("sum_of_price").field("price"))
                .addAggregation(AggregationBuilders.max("max_of_price").field("price"))
                .addAggregation(AggregationBuilders.min("min_of_price").field("price"));

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();
        Aggregations aggregation = sr.getAggregations();

        Sum aggSum = aggregation.get("sum_of_price");
        Max aggMax = aggregation.get("max_of_price");
        Min aggMin = aggregation.get("min_of_price");

        logger.info("sum {} max {} min {}", aggSum.getValue(), aggMax.getValue(), aggMin.getValue());
    }

    @Test
    public void _02_NestedAggQueryTest() throws Exception {
        AggregationBuilder productsAggregationBuilder = AggregationBuilders
                .nested("agg_products", "products")
                .subAggregation(AggregationBuilders.terms("term_of_products_label").field("products.label"))
                .subAggregation(AggregationBuilders.sum("sum_of_price").field("products.price"))
                .subAggregation(AggregationBuilders
                        .nested("inner_agg_products_review", "products.review")
                        .subAggregation(AggregationBuilders.sum("inner_sum_of_star").field("products.review.star")));

        AggregationBuilder productsReviewAggregationBuilder = AggregationBuilders
                .nested("agg_products_review", "products.review")
                .subAggregation(AggregationBuilders.sum("sum_of_star").field("products.review.star"));

        SearchRequestBuilder builder = esClient.prepareSearch(sampleIndexName)
                .setTypes(marketTypeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery("name", "nike"))
                .addAggregation(productsAggregationBuilder)
                .addAggregation(productsReviewAggregationBuilder);

        logger.info("GET {}/{}/_search \n{}", sampleIndexName, marketTypeName, builder.toString());
        SearchResponse sr = builder.get();

        Nested nestedProducts = sr.getAggregations().get("agg_products");
        printTermsAgg(nestedProducts.getAggregations().get("term_of_products_label"), logger);
        printSumAgg(nestedProducts.getAggregations().get("sum_of_price"), logger);

        Nested innerNestedProductsReview = nestedProducts.getAggregations().get("inner_agg_products_review");
        printSumAgg(innerNestedProductsReview.getAggregations().get("inner_sum_of_star"), logger);

        Nested nestedProductsReview = sr.getAggregations().get("agg_products_review");
        printSumAgg(nestedProductsReview.getAggregations().get("sum_of_star"), logger);
    }
}
