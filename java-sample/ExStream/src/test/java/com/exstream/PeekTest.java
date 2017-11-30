package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PeekTest {

    final Logger logger = LoggerFactory.getLogger(PeekTest.class);

    @Test
    public void testPeek() {
        // 1번째부터 3번째 사이의 값을 출력
        Stream.of(1,2,3,4,5,6,7,8,9)
                .skip(1)
                .limit(3)
                .peek(x->System.out.println("peek : " + x))
                .forEach(x->System.out.println("forEach : " + x));
    }
}
