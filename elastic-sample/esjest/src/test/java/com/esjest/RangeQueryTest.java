package com.esjest;

import com.esjest.model.Color;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RangeQueryTest extends BaseTest {

    final static Logger logger = LoggerFactory.getLogger(RangeQueryTest.class);
    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {

        List<Color> documentList = new ArrayList<>();

        documentList.add(new Color("1", "red", "2018-03-01 10:10:10.290"));
        documentList.add(new Color("2", "green", "2018-03-02 10:10:10.290"));
        documentList.add(new Color("3", "blue", "2018-03-03 10:10:10.290"));
        documentList.add(new Color("4", "white", "2018-03-03 12:10:10.190"));

        readyForTest(indexName, typeName, "/query/range/RangeDateQueryMapping.json", documentList);
    }

    @Test
    public void _01_DateRangeQuery_테스트() throws Exception {

        String query = getResource("/query/range/RangeDateQuery.json");

        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .addType(typeName)
                .build();

        SearchResult result = jestClient.execute(search);
        List<Color> colorList = result.getSourceAsObjectList(Color.class, false);

        logger.info("{}", colorList);
    }
}
