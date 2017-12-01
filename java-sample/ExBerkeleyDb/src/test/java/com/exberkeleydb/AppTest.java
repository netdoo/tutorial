package com.exberkeleydb;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _0_테스트_준비() {

    }

    @Test
    public void _1_테스트() {
        assertTrue( true );
    }
}
