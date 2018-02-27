package com.exchunkbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemWriter2 implements ItemWriter<String> {

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

