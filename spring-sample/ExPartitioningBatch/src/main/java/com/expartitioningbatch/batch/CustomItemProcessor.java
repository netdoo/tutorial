package com.expartitioningbatch.batch;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class CustomItemProcessor implements ItemProcessor<List<String>, List<String>> {

    @Override
    public List<String> process(List<String> alphabet) throws Exception {
        return alphabet.stream().map(String::toUpperCase).collect(Collectors.toList());
    }
}

