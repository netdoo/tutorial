package com.esjest;

import com.esjest.domain.Alphabet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.action.Action;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);
    public static String indexName = "dummy";
    public static String typeName = "alphabet";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        JestResult deleteIndexResult = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        JestResult createIndexResult = jestClient.execute(new CreateIndex.Builder(indexName).build());

        String mappingJson = getResource("AppTest.Mapping.json");

        PutMapping putMapping = new PutMapping.Builder(indexName, typeName, mappingJson)
                .setParameter(Parameters.REFRESH, true)
                .build();
    }

    @Test
    public void _1_PUT_테스트() throws Exception {

        Alphabet a = new Alphabet("0", "a", Integer.valueOf('a'));

        Index index = new Index.Builder(objectMapper.writeValueAsString(a)).index(indexName).type(typeName).id(a.getDocId()).build();
        JestResult result = jestClient.execute(index);

        logger.info("put response code {}", result.getResponseCode());

        Bulk.Builder bulkBuilder = new Bulk.Builder()
                .defaultIndex(indexName)
                .defaultType(typeName)
                .setParameter(Parameters.REFRESH, true);

        Alphabet b = new Alphabet("1", "b", Integer.valueOf('b'));
        Alphabet c = new Alphabet("2", "c", Integer.valueOf('c'));
        Alphabet d = new Alphabet("3", "d", Integer.valueOf('d'));

        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(b)).index(indexName).type(typeName).id(b.getDocId()).build());
        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(c)).index(indexName).type(typeName).id(c.getDocId()).build());
        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(d)).index(indexName).type(typeName).id(d.getDocId()).build());

        Bulk bulk = bulkBuilder.build();
        JestResult bulkResult = jestClient.execute(bulk);
        logger.info("bulkResult response code {}", bulkResult.getResponseCode());
    }

    @Test
    public void _2_GET_테스트() throws Exception {

        String query = getResource("AppTest.GetTest.json");

        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType(typeName).build();

        JestResult result = jestClient.execute(search);
        JsonObject hits = (JsonObject)result.getJsonObject().get("hits");

        JsonElement total = hits.get("total");
        JsonArray jsonArray = (JsonArray) hits.get("hits");

        for (int i  = 0; i < jsonArray.size(); i++ ) {
            JsonObject object = (JsonObject)jsonArray.get(i);
            JsonObject source = (JsonObject)object.get("_source");

            String jsonText = source.toString();

            Alphabet alphabet = objectMapper.readValue(source.toString(), Alphabet.class);
            logger.info("{}", source.toString());
        }
    }

    @AfterClass
    public static void 테스트_종료() throws Exception {
        jestClient.shutdownClient();
    }
}
