package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParallelTest {

    final Logger LOGGER = LoggerFactory.getLogger(ParallelTest.class);

    List<String> getFakeResult(int i) {
        switch (i) {
        case 0:
            return Arrays.asList("1", "2");
        case 1:
            return Arrays.asList("3", "4");
        case 2:
            return Arrays.asList("5", "6");
        case 3:
            return Arrays.asList("7", "8");
        }

        return new ArrayList<>();
    }

    ForkJoinPool forkJoinPool = new ForkJoinPool(10);

    @Test
    public void testApp() {

        List<Integer> all = IntStream.range(0, 3).mapToObj(Integer::new).collect(Collectors.toList());
        List<String> results = null;
        try {
            results = forkJoinPool.submit(() -> {
                return all.parallelStream()
                        .flatMap(i -> {
                            List<String> partResult = getFakeResult(i);
                            LOGGER.info("{} => {}", Thread.currentThread().getName(), partResult);
                            return partResult.stream();
                        }).collect(Collectors.toList());
            }).get();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        LOGGER.info("{}", results);
    }
}
