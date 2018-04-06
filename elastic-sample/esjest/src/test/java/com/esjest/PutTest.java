package com.esjest;

import com.esjest.model.Color;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PutTest extends BaseTest {

    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        readyForTest(indexName, typeName, "/query/put/PutQueryMapping.json");
    }

    @Test
    public void _01_PUT_테스트() throws Exception {
        Color red = new Color("1", "red", "2018-03-01 10:10:10.290");

        Index index = new Index.Builder(objectMapper.writeValueAsString(red)).index(indexName).type(typeName).id(red.getDocId()).build();
        JestResult result = jestClient.execute(index);

        logger.info("put response code {}", result.getResponseCode());
    }

    final static Logger logger = LoggerFactory.getLogger(PutTest.class);
}
