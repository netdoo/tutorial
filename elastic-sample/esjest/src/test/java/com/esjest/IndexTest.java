package com.esjest;

import com.esjest.model.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexTest extends BaseTest {

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

        readyForTest(indexName, typeName, "/query/index/IndexQueryMapping.json", documentList);
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

        red.setDocId("1");
        red.setName("LightRed");

        Map<String, Object> doc = new HashMap<>();
        doc.put("doc", red);

        Update update = new Update.Builder(objectMapper.writeValueAsString(doc)).index(indexName).type(typeName).id(red.getDocId()).refresh(true).build();
        JestResult result = jestClient.execute(update);

        logger.info("update response code {}", result.getResponseCode());
    }

    @Test
    public void _03_Get_테스트() throws Exception {

        Get get = new Get.Builder(indexName, "1").build();
        JestResult result = jestClient.execute(get);

        Color color = result.getSourceAsObject(Color.class);
        logger.info("get response code {}, color {}", result.getResponseCode(), color);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(getClass());
}
