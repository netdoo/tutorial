package com.esquery6.dsl.aggquery;

import com.esquery6.BaseTest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BucketSelectorTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(AggQueryTest.class);
    final static String indexName = "sample";
    final static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {
    }

    @Test
    public void _01_ExcludeEmptyBucketTest() throws Exception {
        AggregationBuilder agg = AggregationBuilders
                .terms("agg_pid").field("pid")
                .subAggregation(
                        AggregationBuilders
                                .terms("agg_name")
                                .field("name")
                                .minDocCount(2)
                )
                .subAggregation(
                        PipelineAggregatorBuilders.bucketSelector(
                                "min_bucket_selector",
                                new HashMap<String, String>() {{
                                    put("count", "agg_name._bucket_count");
                                }},
                                new Script("params.count != 0")
                        )
                );

        SearchRequestBuilder builder = esClient.prepareSearch(indexName)
                .setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchAllQuery())
                .addAggregation(agg)
                .setSize(0);

        logger.info("GET {}/{}/_search \n{}", indexName, typeName, builder.toString());
        SearchResponse sr = builder.get();
        Aggregations aggregation = sr.getAggregations();

        Terms terms = aggregation.get("agg_pid");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            logger.info("{} : {} ", bucket.getKeyAsString(), bucket.getDocCount());
            printTermsAgg(bucket.getAggregations().get("agg_name"), logger);
            logger.info("====================");
        }
    }
}
