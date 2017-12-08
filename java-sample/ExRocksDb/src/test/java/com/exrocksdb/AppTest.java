package com.exrocksdb;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.rocksdb.RocksDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    static {
        String libPath = System.getProperty("java.library.path");
        logger.info("{}", libPath);
        RocksDB.loadLibrary();
    }

    @Test
    public void testApp() {

        assertTrue( true );
    }
}
