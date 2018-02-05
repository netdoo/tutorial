package com.esquery6.dsl.aggquery;

import com.esquery6.BaseTest;
import com.esquery6.dsl.termquery.TermQueryTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggQueryTest extends BaseTest {
    final static Logger logger = LoggerFactory.getLogger(AggQueryTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {
        printNodes(logger);
        initSearchTest(logger);
    }

    @Test
    public void _01_() throws Exception {

    }
}
