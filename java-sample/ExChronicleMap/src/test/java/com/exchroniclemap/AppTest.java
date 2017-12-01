package com.exchroniclemap;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    @Test
    public void testApp()
    {
        assertTrue( true );
    }
}
