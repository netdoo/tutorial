package com.exchunkbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<String, String> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String process(String alphabet) throws Exception {
        String processData = alphabet.toLowerCase();
        logger.info("process {} => {}", alphabet, processData);
        return processData;
    }
}

