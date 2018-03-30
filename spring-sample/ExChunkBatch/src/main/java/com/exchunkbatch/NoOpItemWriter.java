package com.exchunkbatch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class NoOpItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List<? extends T> list) throws Exception {

    }
}
