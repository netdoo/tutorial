package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class CrudSample {
    final static Logger logger = LoggerFactory.getLogger(CrudSample.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    public static void main( String[] args ) throws Exception {

        // 기존 embedded db file 지우기
        Arrays.stream(new File(dbPath).listFiles()).forEach(File::delete);

        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));

        // add
        env.executeInTransaction(txn -> {
            // 명시적으로 ReadonlyTransaction 을 제외한
            // 모든 트랜잭션은 exclusive 하게 동작함.
            store.add(txn, stringToEntry("1"), stringToEntry("Red"));
            store.add(txn, stringToEntry("2"), stringToEntry("Green"));
            store.add(txn, stringToEntry("3"), stringToEntry("Blue"));
        });

        // read
        String color = env.computeInReadonlyTransaction(txn -> {
            ByteIterable result = store.get(txn, stringToEntry("1"));
            // 키 값이 없는 경우, get 메서드는 null을 반환함.
            ByteIterable nullResult = store.get(txn, stringToEntry("100"));
            return entryToString(result);
        });

        logger.info("read key 1 / color is {}", color);

        // delete
        env.executeInTransaction(txn -> {
            store.delete(txn, stringToEntry("1"));
        });

        // update
        env.executeInTransaction(txn -> {
            // put 과 add 의 차이점.
            // put은 기존 데이터가 있는 경우, 덮어쓰고,
            // add는 기존 데이터가 있는 경우, 덮어쓰지 않고, false 를 반환함.
            store.put(txn, stringToEntry("3"), stringToEntry("LightBlue"));
        });

        // count
        // execute 트랜잭션은 반환값이 없으나,
        // compute 트랜잭션은 반환값이 있음.
        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("count {}", count);

        // iterate
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    logger.info("iter {} / {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });

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
                    store.add(txn, stringToEntry("k"+mInt.getValue()), stringToEntry("v"+mInt.getValue()));
                }
            });
        }

        stopWatch.stop();
        count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("count {} elapsed time {} (secs)", count, stopWatch.getTime(TimeUnit.SECONDS));

        env.close();
    }
}
