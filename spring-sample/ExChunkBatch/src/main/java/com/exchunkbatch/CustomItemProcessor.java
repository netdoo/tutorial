package com.exchunkbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String alphabet) throws Exception {
        return alphabet.toUpperCase();
    }
}

