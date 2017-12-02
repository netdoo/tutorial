package com.exrocksdb;

import org.junit.Test;
import org.rocksdb.RocksDB;

import static junit.framework.Assert.assertTrue;

public class AppTest {
    static {
        System.load("E:\\tutorial\\java-sample\\ExRocksDb\\librocksdbjni-win64.dll");
    }
    @Test
    public void testApp()
    {
        RocksDB.loadLibrary();
        assertTrue( true );
    }
}
