package com.exleveldb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.iq80.leveldb.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    String dbDir = "C:\\temp\\db";

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(dbDir));
    }

    @Test
    public void _1_BULK_테스트() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        DB db;
        Options options = new Options();
        options.createIfMissing(true);
        db = factory.open(new File(dbDir),options);

        String key = StringUtils.leftPad("0", 64);
        String val = StringUtils.leftPad("0", 2048);

        int count = 0;
        WriteBatch batch = db.createWriteBatch();
        WriteOptions writeOptions = new WriteOptions();
        writeOptions.sync(false);

        for (int i = 0; i < 18_000_000; i++) {
            count++;
            String newKey = key+i;
            batch.put(bytes(String.valueOf(i)), bytes(val));
            if (count % 200_000 == 0) {
                db.write(batch, writeOptions);
                batch.close();
                logger.info("put {} records", count);
                batch = db.createWriteBatch();
            }
        }

        db.write(batch, writeOptions);
        batch.close();
        db.close();
        stopWatch.stop();

        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
