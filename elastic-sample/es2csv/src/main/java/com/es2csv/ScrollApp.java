package com.es2csv;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Query;

import java.io.FileWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class ScrollApp {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    static final String INDEX = "cafe";
    static final String TYPE = "menu";
    static final String HOST = "127.0.0.1";
    static final int PORT = 9300;
    static final String QUERY_JSON_FILE = "C:\\temp\\search.json";
    static final String CSV_OUT_FILE = "C:\\temp\\out.csv";

    public static void list2csv(List list, StringBuilder csv) {
        list.forEach(item -> {
            if (item instanceof Map) {
                map2csv((Map) item, csv);
            } else {
                csv.append(item.toString()).append(",");
            }
        });
    }

    public static void map2csv(Map<String, Object> map, StringBuilder csv) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object v = entry.getValue();
            if (v instanceof Map) {
                map2csv((Map<String, Object>)v, csv);    // nested object 인 경우, 재귀로 csv 덤프
            } else if (v instanceof ArrayList) {
                list2csv((List)v, csv);
            } else {
                csv.append(v.toString()).append(",");
            }
        }
    }

    public static void main(String[] args) {

        try (FileWriter csvWriter = new FileWriter(CSV_OUT_FILE)) {

            Settings settings = Settings.builder()
                    .put("cluster.name", "my-elastic").build();

            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

            FieldSortBuilder sortBuilder = SortBuilders.fieldSort("_id").order(SortOrder.ASC);

            SearchResponse r = client.prepareSearch(INDEX)
                    .setTypes(TYPE)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                    .addSort("_uid", SortOrder.ASC)
                    .setQuery(QueryBuilders.wrapperQuery(new String(Files.readAllBytes(Paths.get(QUERY_JSON_FILE)), StandardCharsets.UTF_8)))
                    .setScroll(new TimeValue(60000))
                    .setSize(2) //max of 2 hits will be returned for each scroll
                    .get();

            StringBuilder csv = new StringBuilder();
            // Scroll until no hits are returned
            do {
                for (SearchHit hit : r.getHits().getHits()) {
                    csv.setLength(0);
                    csv.append(hit.getId()).append(",");
                    map2csv(hit.getSource(), csv);
                    csvWriter.append(csv.replace(csv.length()-1, csv.length(), "\n").toString());
                    logger.info("{}", csv.toString());
                }

                logger.info("search next scroll id {}", r.getScrollId());
                r = client.prepareSearchScroll(r.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            } while(r.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
