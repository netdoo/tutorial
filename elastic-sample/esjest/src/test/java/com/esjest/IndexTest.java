package com.esjest;

import com.esjest.model.Color;
import io.searchbox.client.JestResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexTest extends BaseTest {

    public static String indexName = "color";
    public static String typeName = "data";

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        readyForTest(indexName, typeName, "/query/index/PutQueryMapping.json");
    }

    @Test
    public void _01_PUT_테스트() throws Exception {
        Color red = new Color("1", "red", "2018-03-01 10:10:10.290");

        Index index = new Index.Builder(objectMapper.writeValueAsString(red)).index(indexName).type(typeName).id(red.getDocId()).build();
        JestResult result = jestClient.execute(index);

        logger.info("put response code {}", result.getResponseCode());
    }

    @Test
    public void _02_Update_테스트() throws Exception {
        Color red = new Color();

        red.setName("LightRed");

        Update update = new Update.Builder(objectMapper.writeValueAsString(red)).index(indexName).type(typeName).id(red.getDocId()).build();
        JestResult result = jestClient.execute(update);

        logger.info("update response code {}", result.getResponseCode());
    }

    @Test
    public void _03_Get_테스트() throws Exception {

        Get get = new Get.Builder(indexName, "1").build();
        JestResult result = jestClient.execute(get);
        logger.info("get response code {}", result.getResponseCode());
    }

    Logger logger = LoggerFactory.getLogger(getClass());
}
