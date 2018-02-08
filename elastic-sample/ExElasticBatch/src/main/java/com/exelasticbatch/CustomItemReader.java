package com.exelasticbatch;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.exelasticbatch.model.Alphabet;
import com.exelasticbatch.model.EsBatchContext;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class CustomItemReader implements ItemReader<List<Alphabet>>, StepExecutionListener {

    TransportClient esClient;
    SearchResponse sr;

    @Autowired
    EsBatchContext esBatchContext;

    static final Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    @Override
    public List<Alphabet> read() throws Exception, UnexpectedInputException, ParseException {

        if (sr.getHits().getHits().length == 0) {
            return null;
        }

        List<Alphabet> alphabets = new ArrayList<>(sr.getHits().getHits().length);

        for (SearchHit hit : sr.getHits().getHits()) {
            Alphabet alphabet = esBatchContext.objectMapper.readValue(hit.getSourceAsString(), Alphabet.class);
            alphabet.setDocId(hit.getId());
            alphabets.add(alphabet);
        }

        sr = esClient.prepareSearchScroll(sr.getScrollId()).setScroll(new TimeValue(6, TimeUnit.SECONDS)).execute().actionGet();
        return alphabets;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Settings settings = Settings.builder()
                .put("cluster.name", EsBatchContext.clusterName).build();

        try {
            esClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(EsBatchContext.host), EsBatchContext.port));
        } catch (Exception e) {
            logger.error("fail to connect es ", e);
        }

        SearchRequestBuilder builder = esClient.prepareSearch(EsBatchContext.indexName)
                .setTypes(EsBatchContext.typeName)
                .setScroll(new TimeValue(60000))
                .setQuery(matchAllQuery())
                .addSort(SortBuilders.fieldSort("_id").order(SortOrder.ASC))
                .setSize(10);       // 10개씩 반복해서 검색 결과를 조회함. (최대 검색 결과는 10개)

        logger.info("Request Query\nGET {}/{}/_search\n{}", EsBatchContext.indexName, EsBatchContext.typeName, builder.toString());
        sr = builder.execute().actionGet();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        stepExecution.getJobExecution().getExecutionContext().putString("message", "hello world");
        return null;
    }
}

