package com.exchunkbatch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<String> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void write(List<? extends String> alphabet) throws Exception {

        if (alphabet.size() == 0) {
            logger.info("write data is empty");
        } else {
            logger.info("{}", alphabet);
        }
    }
}

