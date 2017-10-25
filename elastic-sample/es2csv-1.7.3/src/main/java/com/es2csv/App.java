package com.es2csv;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    static final String INDEX = "cafe";
    static final String TYPE = "menu";
    static final String HOST = "127.0.0.1";
    static final int PORT = 9300;
    static final String QUERY_JSON_FILE = "C:\\temp\\search.json";
    static final String CSV_OUT_FILE = "C:\\temp\\out.csv";

    static long getNestedObjCnt(List list) {
        return list.stream().filter(item -> item instanceof Map).count();
    }

    public static void list2csv(List list, StringBuilder csv) {
        if (getNestedObjCnt(list) > 0) {
            list.forEach(item -> {
                if (item instanceof Map) {
                    map2csv((Map) item, csv);
                } else {
                    csv.append('"').append(item.toString()).append('"').append(",");
                }
            });
        } else {
            String s = (String) list.stream().map(Object::toString).collect(Collectors.joining(","));
            csv.append('"').append(s).append('"').append(",");
        }
    }

    public static void map2csv(Map<String, Object> map, StringBuilder csv) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object v = entry.getValue();
            if (v instanceof Map) {
                map2csv((Map<String, Object>)v, csv);    // nested object 인 경우, 재귀로 csv 덤프
            } else if (v instanceof ArrayList) {
                list2csv((List)v, csv);
            } else {
                csv.append('"').append(v.toString()).append('"').append(",");
            }
        }
    }

    public static void field2csv(Map<String, SearchHitField> map, StringBuilder csv) {
        for (Map.Entry<String, SearchHitField> entry : map.entrySet()) {
            csv.append('"').append(entry.getValue().values().stream().map(Object::toString).collect(Collectors.joining(","))).append('"').append(",");
        }
    }

    public static void makeHeader(SearchHit hit, StringBuilder header) {
        header.append("_uid,");
        map2title(hit.getSource(), header, "");
    }

    public static void makeHeader(Map<String, SearchHitField> fields, StringBuilder header) {
        fields.forEach((k, v) -> {
            header.append(k).append(",");
        });
    }

    public static void list2title(List list, StringBuilder header, String parent) {
        if (getNestedObjCnt(list) > 0) {
            list.forEach(item -> {
                if (item instanceof Map) {
                    map2title((Map) item, header, parent);
                }
            });
        } else {
            header.append(parent).append(",");
        }
    }

    static String concatName(String parent, String child) {
        return Optional.ofNullable(parent).orElse("").isEmpty() ? child : parent + "." + child;
    }

    public static void map2title(Map<String, Object> map, StringBuilder header, String parent) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object v = entry.getValue();
            if (v instanceof Map) {
                map2title((Map<String, Object>)v, header, concatName(parent, entry.getKey()));    // nested object 인 경우, 재귀로 csv 덤프
            } else if (v instanceof ArrayList) {
                list2title((List)v, header, concatName(parent, entry.getKey()));
            } else {
                header.append(concatName(parent, entry.getKey())).append(",");
            }
        }
    }

    public static void main(String[] args) {

        try (FileWriter csvWriter = new FileWriter(CSV_OUT_FILE)) {

            Settings settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", "my-elastic").build();

            TransportClient client = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

            FieldSortBuilder sortBuilder = SortBuilders.fieldSort("_id").order(SortOrder.ASC);

            SearchRequestBuilder builder = client.prepareSearch(INDEX)
                    .setTypes(TYPE)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .addSort("_uid", SortOrder.ASC)
                    .setQuery(QueryBuilders.wrapperQuery(new String(Files.readAllBytes(Paths.get(QUERY_JSON_FILE)), StandardCharsets.UTF_8)))
                    .setScroll(new TimeValue(60000))
                    .setSize(2); //max of 20 hits will be returned for each scroll

            builder.setFetchSource(new String[]{"name", "price", "extra.size"}, null);

            SearchResponse r = builder.execute().get();
            int sumOfFetchCount = 0;
            boolean once = false;
            StringBuilder csv = new StringBuilder();
            // Scroll until no hits are returned
            do {
                for (SearchHit hit : r.getHits().getHits()) {
                    if (!once) {
                        csv.append("_uid,");

                        if (hit.fields().size() > 0) {
                            makeHeader(hit.fields(), csv);
                        } else {
                            makeHeader(hit, csv);
                        }

                        csv.replace(csv.length()-1, csv.length(), "\r\n");
                        once = true;
                        csvWriter.append(csv.toString());
                    }

                    sumOfFetchCount++;
                    csv.setLength(0);
                    csv.append('"').append(hit.getId()).append('"').append(",");

                    if (hit.fields().size() > 0) {
                        field2csv(hit.fields(), csv);
                    } else {
                        map2csv(hit.getSource(), csv);
                    }

                    csvWriter.append(csv.replace(csv.length()-1, csv.length(), "\r\n").toString());
                }

                logger.info("fetch {}", sumOfFetchCount);
                r = client.prepareSearchScroll(r.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            } while(r.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
