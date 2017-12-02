package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import net.openhft.chronicle.map.ChronicleMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

            count = env.computeInReadonlyTransaction(txn -> store.count(txn));
            logger.info("process {}", count);
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



    static void bulkInsert() {
        File file = new File("C:\\temp\\cmap.dat");

        if (file.exists())
            file.delete();

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                // Entry checksums make sense only for persisted Chronicle Maps, and are ON by
                // default for such maps
                .entries(5_000_000)
                .averageKeySize(100)
                .averageValueSize(100)
                .createPersistedTo(file)) {

            for (int i = 0; i < 2_000_000; i++) {
                map.put("123456789"+i, "012345678901234567890123456789");
            }

            logger.info("chronicle map size {}", map.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        logger.info("chronicle map elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }

    static void bigTransaction() throws Exception {

        // 기존 embedded db file 지우기
        File dir = new File(dbPath);

        if (dir.exists()) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
        } else {
            dir.mkdir();
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));
        long count = 0;

        // bulk insert
        String dummyKey = StringUtils.leftPad("0", 512);
        String dummyVal = StringUtils.leftPad("0", 2048);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        /*
        for (int mainLoop = 0; mainLoop < 20; mainLoop++) {
            env.executeInTransaction(txn -> {
                for (int i = 0; i < 100_000; i++, atomicInteger.incrementAndGet()) {
                    store.put(txn, stringToEntry(dummyKey + atomicInteger.get()), stringToEntry(dummyVal + atomicInteger.get()));
                }
            });

            count = env.computeInReadonlyTransaction(txn -> store.count(txn));
            logger.info("process {}", count);
        }
        */


        for (int mainLoop = 0; mainLoop < 1_000; mainLoop++) {
            env.executeInTransaction(txn -> {
                for (int i = 0; i < 2_000; i++, atomicInteger.incrementAndGet()) {
                    store.put(txn, stringToEntry(dummyKey + atomicInteger.get()), stringToEntry(dummyVal + atomicInteger.get()));
                }
            });

            count = env.computeInReadonlyTransaction(txn -> store.count(txn));
            logger.info("process {}", count);
        }


        stopWatch.stop();
        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("bigTransaction count {} elapsed time {} (secs)", count, stopWatch.getTime(TimeUnit.SECONDS));
        env.close();
    }

    public static void main( String[] args ) throws Exception {

        //fastTransaction(env, store);
        //slowTransaction(env, store);
        bigTransaction();
        //bulkInsert();
    }
}
