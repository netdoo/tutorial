package com.exrocksdb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    String dbDir = "C:\\temp\\db";
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    private static final CompressionType COMPRESSION_TYPE = CompressionType.NO_COMPRESSION;
    private static final CompactionStyle COMPACTION_STYLE = CompactionStyle.UNIVERSAL;
    private static final long WRITE_BUFFER_SIZE = 128 * 1024 * 1024L;
    private static final long BLOCK_CACHE_SIZE = 500 * 1024 * 1024L;
    private static final long BLOCK_SIZE = 1024L;
    private static final int MAX_WRITE_BUFFERS = 3;

    static {
        RocksDB.loadLibrary();
    }

    long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024);
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        FileUtils.deleteDirectory(new File(dbDir));
    }

    @Test
    public void _1_BULK_테스트() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Options options = new Options()
                .setCreateIfMissing(true)
//                .setWriteBufferSize(WRITE_BUFFER_SIZE)
//                .setMaxWriteBufferNumber(MAX_WRITE_BUFFERS)
//                .setCompressionType(COMPRESSION_TYPE)
//                .setCompactionStyle(COMPACTION_STYLE)
                .prepareForBulkLoad()
                ;

        WriteOptions writeOptions = new WriteOptions();
        writeOptions.setDisableWAL(true);
        writeOptions.setSync(false);

//        final BlockBasedTableConfig tableConfig = new BlockBasedTableConfig();
//        tableConfig.setBlockCacheSize(BLOCK_CACHE_SIZE);
//        tableConfig.setBlockSize(BLOCK_SIZE);
//
//        options.setTableFormatConfig(tableConfig);
  //      options.setIncreaseParallelism(Math.max(Runtime.getRuntime().availableProcessors(), 2));

        RocksDB db = RocksDB.open(options, dbDir);

        String key = StringUtils.leftPad("0", 64);
        String val = StringUtils.leftPad("0", 1024);

        int count = 0;
        WriteBatch batch = new WriteBatch();

        String newKey;

        for (int i = 0; i < 20_000_000; i++) {
            count++;
            newKey = key+i;
            batch.put(newKey.getBytes(),val.getBytes());

            if (count % 20_000 == 0) {
                db.write(writeOptions, batch);
                batch.close();
//                logger.info("put {} records, used memory {} (mb)", count, getUsedMemory());
                logger.info("put {} records", count);
                batch = new WriteBatch();
            }
        }

        db.write(writeOptions, batch);
        batch.close();
        db.close();

        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
