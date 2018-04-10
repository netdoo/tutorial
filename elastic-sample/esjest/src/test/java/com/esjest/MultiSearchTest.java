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
public class MultiSearchTest extends BaseTest {

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

        readyForTest(indexName, typeName, "/query/multisearch/MultiSearchMapping.json", documentList);
    }

    @Test
    public void _01_MultiSearch_테스트() throws Exception {

        String termQuery = getResource("/query/multisearch/TermQuery.json");
        String rangeQuery = getResource("/query/multisearch/RangeDateQuery.json");


        Search termSearch = new Search.Builder(termQuery).addIndex(indexName).addType(typeName).build();
        Search rangeSearch = new Search.Builder(rangeQuery).addIndex(indexName).addType(typeName).build();

        MultiSearch multiSearch = new MultiSearch.Builder(Arrays.asList(termSearch, rangeSearch)).build();
        MultiSearchResult result = jestClient.execute(multiSearch);

        List<MultiSearchResult.MultiSearchResponse> responses = result.getResponses();

        for (MultiSearchResult.MultiSearchResponse response : responses) {
            List<Color> colors = response.searchResult.getSourceAsObjectList(Color.class, false);
            logger.info("{}", colors);
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(getClass());
}
