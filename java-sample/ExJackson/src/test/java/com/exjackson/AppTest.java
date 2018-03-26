package com.exjackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.commons.io.IOUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    @Test
    public void _01_ReadFromJsonFileTest() throws Exception {
        JsonFactory jfactory = new JsonFactory();
        String jsonText = IOUtils.toString(this.getClass().getResourceAsStream("/sampleFile.json"), "UTF-8");
        JsonParser jParser = jfactory.createParser(jsonText.getBytes());

        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jParser.getCurrentName();

            if ("name".equals(fieldName)) {
                jParser.nextToken();
                logger.info("문자열 : {}", jParser.getText()); // 문자열인 경우
            } else if ("age".equals(fieldName)) {
                jParser.nextToken();
                logger.info("숫자 : {}", jParser.getIntValue()); // 숫자인 경우
            } else if ("messages".equals(fieldName)) {
                jParser.nextToken(); // current token is "[", move next
                // messages is array, loop until token equal to "]"
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    // 문자열 배열인 경우
                    logger.info("문자열 배열 {}", jParser.getText());
                }
            }
        }
        jParser.close();
    }

    Logger logger = LoggerFactory.getLogger(getClass());
}
