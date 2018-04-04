package com.esjest;

import com.esjest.domain.Alphabet;
import com.esjest.domain.Color;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.scenario.Settings;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.params.Parameters;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RangeQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(RangeQueryTest.class);
    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        JestResult deleteIndexResult = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        assertTrue(deleteIndexResult.getErrorMessage(), deleteIndexResult.isSucceeded());

        JestResult createIndexResult = jestClient.execute(new CreateIndex.Builder(indexName).build());
        assertTrue(createIndexResult.getErrorMessage(), createIndexResult.isSucceeded());

        String mappingJson = getResource("RangeQueryTest.Mapping.json");

        PutMapping putMapping = new PutMapping.Builder(indexName, typeName, mappingJson)
                .build();

        JestResult result = jestClient.execute(putMapping);
        assertTrue(result.getErrorMessage(), result.isSucceeded());

        Bulk.Builder bulkBuilder = new Bulk.Builder()
                .defaultIndex(indexName)
                .defaultType(typeName)
                .setParameter(Parameters.REFRESH, true);

        Color a = new Color("1", "red", "2018-03-01 10:10:10.290");
        Color b = new Color("2", "green", "2018-03-02 10:10:10.290");
        Color c = new Color("3", "blue", "2018-03-03 10:10:10.290");
        Color d = new Color("4", "white", "2018-03-03 12:10:10.190");

        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(a)).index(indexName).type(typeName).id(b.getDocId()).build());
        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(b)).index(indexName).type(typeName).id(b.getDocId()).build());
        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(c)).index(indexName).type(typeName).id(c.getDocId()).build());
        bulkBuilder.addAction(new Index.Builder(objectMapper.writeValueAsString(d)).index(indexName).type(typeName).id(d.getDocId()).build());

        Bulk bulk = bulkBuilder.build();
        JestResult bulkResult = jestClient.execute(bulk);
        logger.info("bulkResult response code {}", bulkResult.getResponseCode());
    }

    @Test
    public void _01_DateRangeQuery_테스트() throws Exception {

        String query = getResource("RangeQueryTest.DateTimeQuery.json");

        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType(typeName)
                .build();

        SearchResult result = jestClient.execute(search);
        JsonObject hits = (JsonObject)result.getJsonObject().get("hits");

        JsonElement total = hits.get("total");
        JsonArray jsonArray = (JsonArray) hits.get("hits");

        for (int i  = 0; i < jsonArray.size(); i++ ) {
            JsonObject object = (JsonObject)jsonArray.get(i);
            JsonObject source = (JsonObject)object.get("_source");
            logger.info("{}", source.toString());
        }
    }
}
