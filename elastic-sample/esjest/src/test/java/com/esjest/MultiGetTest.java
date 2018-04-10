package com.esjest;

import com.esjest.model.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultiGetTest extends BaseTest {

    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        List<Color> documentList = new ArrayList<>();

        documentList.add(new Color("1", "red", "2018-03-01 10:10:10.290"));
        documentList.add(new Color("2", "green", "2018-03-02 10:10:10.290"));
        documentList.add(new Color("3", "blue", "2018-03-03 10:10:10.290"));
        documentList.add(new Color("4", "white", "2018-03-03 15:10:10.190"));
        documentList.add(new Color("5", "black", "2018-03-04 12:10:10.190"));
        documentList.add(new Color("6", "lemon", "2018-03-05 12:10:10.190"));

        readyForTest(indexName, typeName, "/query/multiget/MultiGetMapping.json", documentList);
    }

    @Test
    public void _01_MultiGet_테스트() throws Exception {

        List<Doc> docs = Arrays.asList(
                new Doc(indexName, typeName, "1"),
                new Doc(indexName, typeName, "2"),
                new Doc(indexName, typeName, "3"),
                new Doc(indexName, typeName, "4"),
                new Doc(indexName, typeName, "5"),
                new Doc(indexName, typeName, "6")
        );

        MultiGet multiGet = new MultiGet.Builder.ByDoc(docs).build();
        JestResult result = jestClient.execute(multiGet);
        JsonArray actualDocs = result.getJsonObject().getAsJsonArray("docs");

        for (JsonElement element : actualDocs) {
            JsonObject jsonObject = element.getAsJsonObject();
            String id = jsonObject.get("_id").toString();

            if (Boolean.valueOf(jsonObject.get("found").toString())) {
                String source = jsonObject.get("_source").toString();
                Color color = objectMapper.readValue(source, Color.class);
                logger.info("id : {}, {}", id, color);
            } else {
                logger.info("not found {}", id);
            }
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(getClass());
}
