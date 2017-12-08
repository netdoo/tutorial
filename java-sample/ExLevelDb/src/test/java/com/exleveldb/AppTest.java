package com.exleveldb;

import org.junit.Test;
import org.rocksdb.RocksDB;

import static junit.framework.Assert.assertTrue;

public class AppTest {


    @Test
    public void testApp()
    {
        RocksDB.loadLibrary();
        assertTrue( true );
    }
}
