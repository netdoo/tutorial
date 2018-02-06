package com.esquery6.dsl.document;

import com.esquery6.BaseTest;
import com.esquery6.domain.Market;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class _9_BulkProcessorTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(_9_BulkProcessorTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_BulkTest() throws Exception {

        BulkProcessor bulkProcessor = BulkProcessor.builder(esClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                logger.info("{} {}", l, bulkResponse.status());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

            }
        })
        .setBulkActions(100)
        .setFlushInterval(TimeValue.timeValueSeconds(5))
        .setConcurrentRequests(1)
        .build();

        List<Market> markets = getMarkets();

        markets.forEach(market -> {
            try {
                bulkProcessor.add(new IndexRequest(sampleIndexName, marketTypeName, market.getDocId()).source(objectMapper.writeValueAsString(market), XContentType.JSON));
            } catch (Exception e) {
                logger.error("fail to parse object ", e);
            }
        });

        // Flush any remaining requests
        bulkProcessor.flush();

        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);

        // Or close the bulkProcessor if you don't need it anymore
        bulkProcessor.close();

        // Refresh your indices
        esClient.admin().indices().prepareRefresh(sampleIndexName).get();

        // Now you can start searching!
        SearchResponse sr = esClient.prepareSearch(sampleIndexName).get();
        String name, country, location;
        Integer price;

        long searchCount = sr.getHits().getTotalHits();

        for (SearchHit hit : sr.getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();

            name = (String)source.getOrDefault("name", "");
            price = (Integer)source.getOrDefault("price", 0);
            country = (String)source.getOrDefault("country", "");
            location = (String)source.getOrDefault("location", "");

            logger.info("score : {}, id : {}, name : {}, price : {}, country : {}, location : {}", hit.getScore(), hit.getId(), name, price, country, location);
        }
    }
}
