package com.esjest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchSample {
    final static Logger logger = LoggerFactory.getLogger(MatchSample.class);

    public static void main( String[] args ) throws Exception {

        JestClient jestClient = AppConfig.create();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("brand", "KIA"));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(AppConfig.INDEX)
                .addType(AppConfig.TYPE).build();

        logger.info("{}/{}\n{}", AppConfig.INDEX, AppConfig.TYPE, searchSourceBuilder.toString());

        JestResult result = jestClient.execute(search);
        JsonObject hits = (JsonObject)result.getJsonObject().get("hits");

        JsonElement total = hits.get("total");
        JsonArray jsonArray = (JsonArray) hits.get("hits");

        for (int i  = 0; i < jsonArray.size(); i++ ) {
            JsonObject object = (JsonObject)jsonArray.get(i);
            logger.info("{}", object.toString());
        }

        jestClient.shutdownClient();
    }
}
