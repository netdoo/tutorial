package com.esquery6.dsl.aggquery;

import com.esquery6.BaseTest;
import com.esquery6.dsl.termquery.TermQueryTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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
}
