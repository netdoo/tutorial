package com.exstream;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FindFirstTest {

    final Logger logger = LoggerFactory.getLogger(FindFirstTest.class);

    @Test
    public void testFilter() {
        String result = Stream.of("#Black#", "BLACK",  "", "BLACK", null, "검정","black")
                .filter(text -> {
                    logger.info("Compare BLACK {}", text);
                    return StringUtils.equalsIgnoreCase("BLACK", text);
                })
                .findFirst().orElse("Not Found");

        logger.info("결과 {}", result);
    }
}
