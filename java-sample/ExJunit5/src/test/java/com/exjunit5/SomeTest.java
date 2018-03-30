package com.exjunit5;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnitPlatform.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SomeTest {

    final static Logger logger = LoggerFactory.getLogger(SomeTest.class);

    @BeforeAll
    static void onBeforeAll() {
        logger.info("executes once before all test methods in this class");

    }

    @BeforeEach
    void onBeforeEach() {
        logger.info("executes before each test method in this class");
    }

    @AfterEach
    void onAfterEach() {
        logger.info("onAfterEach");
    }

    @AfterAll
    static void onAfterAll() {
        logger.info("after all");
    }

    @Test
    @DisplayName("f.o.o.t.e.s.t")
    public void fooTest() {
        logger.info("fooTest");
    }

    @Test
    public void barTest() {
        logger.info("barTest");
    }
}
