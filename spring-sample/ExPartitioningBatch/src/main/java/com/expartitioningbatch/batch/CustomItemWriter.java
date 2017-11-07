package com.expartitioningbatch.batch;

import com.expartitioningbatch.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemWriter implements ItemWriter<List<String>> {

    static final Logger logger = LoggerFactory.getLogger(CustomItemWriter.class);

    @Override
    public void write(List<? extends List<String>> alphabet) throws Exception {
        logger.info("CustomItemWriter ThreadId {}, Data {}", Thread.currentThread().getId(), alphabet);
    }
}

