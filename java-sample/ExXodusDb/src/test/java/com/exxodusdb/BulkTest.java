package com.exxodusdb;

import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    String dbDir = "C:\\temp\\db";
    int i = 0, count = 0;

    long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024);
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        File dir = new File(dbDir);

        if (dir.exists()) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
        } else {
            dir.mkdir();
        }
    }

    @Test
    public void _1_BULK_테스트() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Environment env = Environments.newInstance(this.dbDir);
        Store store = env.computeInTransaction(txn -> env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));

        String key = StringUtils.leftPad("0", 64);
        String val = StringUtils.leftPad("0", 1024);

        while (count < 20_000_000) {
            env.executeInTransaction(txn -> {
                for (int i = 0; i < 200_000; i++) {
                    count++;
                    store.put(txn, stringToEntry(key + count), stringToEntry(val));
                }
            });
            logger.info("put {} records", count);
        }
        env.close();
        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
