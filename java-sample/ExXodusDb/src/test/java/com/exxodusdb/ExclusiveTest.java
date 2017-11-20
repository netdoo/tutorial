package com.exxodusdb;

import com.exxodusdb.domain.EPTSVData;
import com.exxodusdb.domain.EPTSVData2;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

/**
 * Created by jhkwon78 on 2017-11-17.
 */
public class ExclusiveTest {

    final Logger LOGGER = LoggerFactory.getLogger(ExclusiveTest.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    class BulkThread extends Thread implements Runnable {
        String name;
        Environment env;
        Store store;
        boolean exclusive = false;

        public BulkThread(String name, Environment env, Store store, boolean exclusive) {
            this.name = name;
            this.env = env;
            this.store = store;
            this.exclusive = exclusive;
        }

        @Override
        public void run() {
            if (this.exclusive) {
                this.env.executeInExclusiveTransaction(txn -> {
                    for (int i = 0; i < 1000; i++) {
                        boolean result = this.store.add(txn, stringToEntry(this.name+i), stringToEntry("012345678901234567890123456789"+i));
                        LOGGER.info("name {} result {}", this.name, result);
                    }
                });
            } else {
                this.env.executeInTransaction(txn -> {
                    for (int i = 0; i < 1000; i++) {
                        boolean result = this.store.add(txn, stringToEntry(this.name+i), stringToEntry("012345678901234567890123456789"+i));
                        LOGGER.info("name {} result {}", this.name, result);
                    }
                });
            }
        }
    }

    @Test
    public void testDefaultTransaction() throws Exception {
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));
        long count = 0;

        BulkThread bulkThread1 = new BulkThread("a", env, store, false);
        BulkThread bulkThread2 = new BulkThread("b", env, store, false);

        bulkThread1.start();
        bulkThread2.start();

        bulkThread1.join();
        bulkThread2.join();

        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        LOGGER.info("deafult transaction count {} ", count);
        env.close();
    }

    @Test
    public void testExclusive() throws Exception {
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));
        long count = 0;

        // transaction을 exclusive 하게 설정하면, 동시에 1개의 스레드만 DB에 접근이 가능함.
        BulkThread bulkThread1 = new BulkThread("a", env, store, true);
        BulkThread bulkThread2 = new BulkThread("b", env, store, true);

        bulkThread1.start();
        bulkThread2.start();

        bulkThread1.join();
        bulkThread2.join();

        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        LOGGER.info("exclusive transaction count {} ", count);
        env.close();
    }
}
