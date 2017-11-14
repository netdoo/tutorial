package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class BulkAddSample {
    final static Logger logger = LoggerFactory.getLogger(BulkAddSample.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    static void fastTransaction(Environment env, Store store) {
        long count = 0;

        // bulk insert
        MutableInt mInt = new MutableInt(1);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 트랜잭션마다 처리할 수 있는 Capacity 를 고려하여,
        // 전체 입력 갯수를 n개의 트랜잭션으로 파티셔닝하여 입력함.
        for (int i = 0; i < 100; i++) {
            env.executeInTransaction(txn -> {
                // 한 트랜잭션에서 10,000개의 데이터를 입력함.
                for (int j = 0; j < 10_000; j++, mInt.increment()) {
                    store.add(txn, stringToEntry("fast"+mInt.getValue()), stringToEntry("fast"+mInt.getValue()));
                }
            });
        }

        stopWatch.stop();
        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("fastTransaction count {} elapsed time {} (secs)", count, stopWatch.getTime(TimeUnit.SECONDS));
    }

    static void slowTransaction(Environment env, Store store) {
        long count = 0;

        // bulk insert
        MutableInt mInt = new MutableInt(1);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 트랜잭션마다 처리할 수 있는 Capacity 를 고려하여,
        // 전체 입력 갯수를 n개의 트랜잭션으로 파티셔닝하여 입력함.
        for (int i = 0; i < 100_000; i++) {
            env.executeInTransaction(txn -> {
                mInt.increment();
                store.add(txn, stringToEntry("slow"+mInt.getValue()), stringToEntry("slow"+mInt.getValue()));
            });
        }

        stopWatch.stop();
        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("slowTransaction count {} elapsed time {} (secs)", count, stopWatch.getTime(TimeUnit.SECONDS));
    }

    static void bigTransaction(Environment env, Store store) {
        long count = 0;

        // bulk insert
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        env.executeInTransaction(txn -> {
            for (int i = 0; i < 2_000_000; i++) {
                store.add(txn, stringToEntry("slow"+i), stringToEntry("slow"+i));
            }
        });

        stopWatch.stop();
        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("slowTransaction count {} elapsed time {} (secs)", count, stopWatch.getTime(TimeUnit.SECONDS));
    }

    public static void main( String[] args ) throws Exception {

        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));

        //fastTransaction(env, store);
        //slowTransaction(env, store);
        bigTransaction(env, store);

        env.close();
    }
}
