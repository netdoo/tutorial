package com.exrocksdb;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.rocksdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static junit.framework.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTest {

    String dbDir = "C:\\temp\\db";
    final static Logger logger = LoggerFactory.getLogger(CRUDTest.class);

    static {
        RocksDB.loadLibrary();
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(dbDir));
    }

    @Test
    public void _1_CRUD_테스트() throws Exception {
        final Options options = new Options().setCreateIfMissing(true);
        RocksDB db = RocksDB.open(options, dbDir);

        // Put
        db.put("001".getBytes(), "RED".getBytes());
        db.put("002".getBytes(), "GREEN".getBytes());
        db.put("003".getBytes(), "BLUE".getBytes());

        logger.info("001 => {}", new String(db.get("001".getBytes())));

        // Update
        db.put("001".getBytes(), "DARKRED".getBytes());

        // Delete
        db.delete("002".getBytes());

        // Get
        logger.info("001 => {}", new String(db.get("001".getBytes())));

        // Batch
        WriteBatch batch = new WriteBatch();
        batch.put("004".getBytes(), "WHITE".getBytes());
        batch.put("005".getBytes(), "BLACK".getBytes());

        db.write(new WriteOptions(), batch);
        batch.close();

        // Iterator
        final RocksIterator iterator = db.newIterator();

        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            logger.info("{} / {}", new String(iterator.key()), new String(iterator.value()));
        }

        iterator.close();
        db.close();
    }
}
