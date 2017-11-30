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
public class FilterTest {

    final Logger logger = LoggerFactory.getLogger(FilterTest.class);

    @Test
    public void testFilter() {
        // 1번째부터 공백, null 등의 문자를 제외하고 출력
        Stream.of("#Black#", "BLACK",  "", " ", null, "검정","black")
                .skip(1)
                .filter(term -> StringUtils.isNoneBlank(Optional.ofNullable(term).orElse("").trim()))
                .map(term -> term.trim().toUpperCase())
                .forEach(term -> System.out.println("forEach : " + term));
    }
}
