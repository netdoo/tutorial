package com.exxodusdb;

import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExclusiveTest {

    final Logger logger = LoggerFactory.getLogger(ExclusiveTest.class);

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
                    for (int i = 0; i < 5000; i++) {
                        boolean result = this.store.add(txn, stringToEntry(this.name+i), stringToEntry("012345678901234567890123456789"+i));
                        logger.info("name {} result {}", this.name, result);
                    }
                });
            } else {
                this.env.executeInTransaction(txn -> {
                    for (int i = 0; i < 5000; i++) {
                        boolean result = this.store.add(txn, stringToEntry(this.name+i), stringToEntry("012345678901234567890123456789"+i));
                        logger.info("name {} result {}", this.name, result);
                    }
                });
            }
        }
    }

    @Test
    public void _0_테스트_준비() throws Exception {
        TestEnv.cleanUp();
    }

    @Test
    public void _1_테스트_기본_트랜잭션() throws Exception {
        Environment env = Environments.newInstance(TestEnv.dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore(TestEnv.storeName, StoreConfig.WITHOUT_DUPLICATES, txn));
        long count = 0;

        BulkThread bulkThread1 = new BulkThread("a", env, store, false);
        BulkThread bulkThread2 = new BulkThread("b", env, store, false);

        bulkThread1.start();
        bulkThread2.start();

        bulkThread1.join();
        bulkThread2.join();

        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("deafult transaction count {} ", count);
        env.close();
    }

    @Test
    public void _2_테스트_Exclusive_트랜잭션() throws Exception {
        Environment env = Environments.newInstance(TestEnv.dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore(TestEnv.storeName, StoreConfig.WITHOUT_DUPLICATES, txn));
        long count = 0;

        // transaction을 exclusive 하게 설정하면, 동시에 1개의 스레드만 DB에 접근이 가능함.
        BulkThread bulkThread1 = new BulkThread("a", env, store, true);
        BulkThread bulkThread2 = new BulkThread("b", env, store, true);

        bulkThread1.start();
        bulkThread2.start();

        bulkThread1.join();
        bulkThread2.join();

        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("exclusive transaction count {} ", count);
        env.close();
    }

    @Test
    public void _99_테스트_종료() throws Exception {

    }
}
