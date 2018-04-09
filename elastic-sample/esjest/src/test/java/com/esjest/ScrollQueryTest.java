package com.esjest;

import com.esjest.model.Color;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchScroll;
import io.searchbox.params.Parameters;
import io.searchbox.params.SearchType;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScrollQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(ScrollQueryTest.class);
    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        List<Color> documentList = new ArrayList<>();

        documentList.add(new Color("1", "red", "2018-03-01 10:10:10.290"));
        documentList.add(new Color("2", "green", "2018-03-02 10:10:10.290"));
        documentList.add(new Color("3", "blue", "2018-03-03 10:10:10.290"));
        documentList.add(new Color("4", "white", "2018-03-03 12:10:10.190"));

        readyForTest(indexName, typeName, "/query/scroll/ScrollQueryMapping.json", documentList);
    }

    @Test
    public void _01_ScrollQuery_테스트() throws Exception {

        String scroll = "5m";
        String query = getResource("/query/scroll/ScrollQuery.json");

        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType(typeName)
                .setParameter(Parameters.SEARCH_TYPE, SearchType.DFS_QUERY_THEN_FETCH)
                .setParameter(Parameters.SIZE, 2)
                .setParameter(Parameters.SCROLL, scroll)
                .build();

        JestResult result = jestClient.execute(search);
        String scrollId = result.getJsonObject().get("_scroll_id").getAsString();
        List<Color> colorList = result.getSourceAsObjectList(Color.class, false);

        while (!colorList.isEmpty()) {
            logger.info("{}", colorList);

            SearchScroll scrollRequest = new SearchScroll.Builder(scrollId, scroll)
                    .build();

            logger.info("fetch next scroll ");
            result = jestClient.execute(scrollRequest);
            scrollId = result.getJsonObject().get("_scroll_id").getAsString();
            colorList = result.getSourceAsObjectList(Color.class, false);
        }
    }
}
