package com.exjunit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jhkwon78 on 2017-11-28.
 */
public class SomeTest {

    /*

    1. SomeTest 클래스의 모든 테스트를 실행
        $ mvn test -Dtest=SomeTest

    2. SomeTest 클래스의 fooTest 만 테스트 함.
        $ mvn test -Dtest=SomeTest#fooTest

    3. SomeTest 클래스의 *Test와 매칭되는 테스트만 테스트 함.
        $ mvn test -Dtest=SomeTest#*Test
     */
    final static Logger logger = LoggerFactory.getLogger(SomeTest.class);

    @BeforeClass
    public static void onBefore() {
        logger.info("call onBefore");
    }

    @AfterClass
    public static void onAfter() {
        logger.info("call onAfter");
    }

    @Test
    public void fooTest() {
        logger.info("fooTest");
    }

    @Test
    public void barTest() {
        logger.info("barTest");
    }
}
