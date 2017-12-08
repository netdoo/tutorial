package com.exleveldb;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertTrue;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.DBIterator;
import java.io.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTest {

    final static Logger logger = LoggerFactory.getLogger(CRUDTest.class);
    String dbDir = "./db";

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(dbDir));
    }

    @Test
    public void _1_CRUD_테스트() throws Exception {
        DB db;
        Options options = new Options();
        options.createIfMissing(true);

        // Create
        db = factory.open(new File(dbDir),options);

        // Put
        db.put(bytes("001"),bytes("RED"));
        db.put(bytes("002"),bytes("GREEN"));
        db.put(bytes("003"),bytes("BLUE"));

        logger.info("001 => {}", new String(db.get(bytes("001"))));

        // Update
        db.put(bytes("003"), bytes("DARKBLUE"));

        // Delete
        db.delete(bytes("001"));

        logger.info("003 => {}", new String(db.get(bytes("003"))));

        // Batch
        WriteBatch batch = db.createWriteBatch();
        batch.put(bytes("004"),bytes("BLACK"));
        batch.put(bytes("005"),bytes("WHITE"));
        batch.delete(bytes("003"));
        db.write(batch);
        batch.close();

        // Iterator
        DBIterator iterator = db.iterator();
        iterator.seekToFirst();

        while (iterator.hasNext()){
            byte[] key = iterator.peekNext().getKey();
            byte[] value = iterator.peekNext().getValue();

            logger.info("iterate key {} / value {}", new String(key), new String(value));
            iterator.next();
        }

        iterator.close();
        db.close();
    }
}
