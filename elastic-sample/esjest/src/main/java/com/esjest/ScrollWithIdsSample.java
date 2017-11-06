package com.esjest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchScroll;
import io.searchbox.params.Parameters;
import io.searchbox.params.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollWithIdsSample {
    final static Logger logger = LoggerFactory.getLogger(ScrollWithIdsSample.class);

    public static void main( String[] args ) throws Exception {

        String scroll = "5m";
        JestClient jestClient = AppConfig.create();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds("1", "2", "3", "4", "5"));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(AppConfig.INDEX)
                .addType(AppConfig.TYPE)
                .setParameter(Parameters.SEARCH_TYPE, SearchType.DFS_QUERY_THEN_FETCH)
                .setParameter(Parameters.SIZE, 3)
                .setParameter(Parameters.SCROLL, scroll)
                .build();

        logger.info("{}/{}\n{}", AppConfig.INDEX, AppConfig.TYPE, searchSourceBuilder.toString());

        JestResult result = jestClient.execute(search);
        JsonObject hits = (JsonObject)result.getJsonObject().get("hits");
        String scrollId = result.getJsonObject().get("_scroll_id").getAsString();
        JsonArray sourceList  = hits.get("hits").getAsJsonArray();

        while (sourceList.size() > 0) {
            sourceList.forEach(jsonObject -> {
                logger.info("{}", jsonObject.toString());
            });

            SearchScroll scrollRequest = new SearchScroll.Builder(scrollId, scroll)
                    .build();

            logger.info("fetch next scroll ");
            result = jestClient.execute(scrollRequest);
            scrollId = result.getJsonObject().get("_scroll_id").getAsString();
            hits = (JsonObject)result.getJsonObject().get("hits");
            sourceList  = hits.get("hits").getAsJsonArray();
        }

        jestClient.shutdownClient();
    }
}
