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

public class TransactionSample {
    final static Logger logger = LoggerFactory.getLogger(TransactionSample.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    public static void main( String[] args ) throws Exception {
        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            // 명시적으로 ReadonlyTransaction 을 제외한
            // 모든 트랜잭션은 exclusive 하게 동작함.
            store.put(txn, stringToEntry("1"), stringToEntry("Red"));
        });

        // add
        env.executeInTransaction(txn -> {
            // 명시적으로 ReadonlyTransaction 을 제외한
            // 모든 트랜잭션은 exclusive 하게 동작함.

            store.delete(txn, stringToEntry("1"));
            ByteIterable exist = store.get(txn, stringToEntry("1"));
            store.put(txn, stringToEntry("1"), stringToEntry("DarkRed"));
        });

        // iterate
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    logger.info("iter {} / {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });

        env.close();
    }
}
