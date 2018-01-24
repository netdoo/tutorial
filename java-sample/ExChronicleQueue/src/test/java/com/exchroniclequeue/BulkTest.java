package com.exchroniclequeue;


import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static long MAX_PUT_COUNT = 1000L;
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    File file = new File("./bulkMap.dat");

    @Test
    public void _0_테스트_준비() {
        if (file.exists())
            file.delete();

        file.deleteOnExit();
    }

    @Test
    public void _1_테스트_BULK_PUT() {

    }
}
