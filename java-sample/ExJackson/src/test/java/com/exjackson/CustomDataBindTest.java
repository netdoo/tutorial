package com.exjackson;

import com.exjackson.model.Box;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomDataBindTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void _01_CustomSerializeTest() throws Exception {
        Box box = new Box("RED", "001");
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonText = objectMapper.writeValueAsString(box);
        logger.info("{}", jsonText);

        Box result = objectMapper.readValue(jsonText, Box.class);
        logger.info("{}", box);
    }
}
