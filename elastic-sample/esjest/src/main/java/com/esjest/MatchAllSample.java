package com.esjest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchAllSample {
    final static Logger logger = LoggerFactory.getLogger(MatchAllSample.class);

    public static void main( String[] args ) throws Exception {

        JestClient jestClient = AppConfig.create();

        String query = "";

        Search search = new Search.Builder(query).addIndex(AppConfig.INDEX)
                .addType(AppConfig.TYPE).build();


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
