package com.esjest;

import com.esjest.model.Color;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountQueryTest extends BaseTest {

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

        readyForTest(indexName, typeName, "/query/count/CountQueryMapping.json", documentList);
    }

    @Test
    public void _01_CountAll_테스트() throws Exception {
        CountResult result = jestClient.execute(new Count.Builder()
                .addIndex(indexName)
                .addType(typeName)
                .build());

        assertTrue(result.getErrorMessage(), result.isSucceeded());
        logger.info("count {}", result.getCount());
    }

    @Test
    public void _02_CountByQuery_테스트() throws Exception {

        String query = getResource("/query/count/CountQuery.json");

        CountResult result = jestClient.execute(new Count.Builder()
                .addIndex(indexName)
                .addType(typeName)
                .query(query)
                .build());

        assertTrue(result.getErrorMessage(), result.isSucceeded());
        logger.info("count {}", result.getCount());
    }

    Logger logger = LoggerFactory.getLogger(getClass());
}
