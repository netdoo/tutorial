package com.exstream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testApp() {
        assertTrue( true );
    }
}
