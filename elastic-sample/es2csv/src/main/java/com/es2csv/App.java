package com.es2csv;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Query;

import java.io.FileWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    static final String INDEX = "cafe";
    static final String TYPE = "menu";
    static final String HOST = "127.0.0.1";
    static final int PORT = 9300;
    static final String QUERY_JSON_FILE = "C:\\temp\\search.json";
    static final String CSV_OUT_FILE = "C:\\temp\\out.csv";

    public static void map2csv(Map<String, Object> map, StringBuilder csv) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map)  {
                map2csv((Map<String, Object>)entry.getValue(), csv);    // nested object 인 경우, 재귀로 csv 덤프
            } else {
                csv.append(entry.getValue().toString()).append(",");
            }
        }
    }

    public static void main(String[] args) {

        try (FileWriter csvWriter = new FileWriter(CSV_OUT_FILE)) {

            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

            SearchResponse r = client.prepareSearch(INDEX)
                    .setTypes(TYPE)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.wrapperQuery(new String(Files.readAllBytes(Paths.get(QUERY_JSON_FILE)), StandardCharsets.UTF_8)))
                    .get();

            StringBuilder csv = new StringBuilder();

            for (SearchHit hit : r.getHits()) {
                csv.setLength(0);
                map2csv(hit.getSource(), csv);
                csvWriter.append(csv.replace(csv.length()-1, csv.length(), "\n").toString());
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
