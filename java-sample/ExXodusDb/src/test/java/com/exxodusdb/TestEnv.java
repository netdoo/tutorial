package com.exxodusdb;

import jetbrains.exodus.env.Cursor;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

import static jetbrains.exodus.bindings.StringBinding.entryToString;

public class TestEnv {

    final static String dbPath = "C:\\Temp\\xodus.db";
    final static String storeName = "DB";
    final static Logger logger = LoggerFactory.getLogger(TestEnv.class);

    public static void cleanUp() {
        File dir = new File(TestEnv.dbPath);

        if (dir.exists()) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
        } else {
            dir.mkdir();
        }
    }

    public static long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024);
    }

    public static void print(Environment env, Store store) {
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    String key = entryToString(cursor.getKey());   // current key
                    String value = entryToString(cursor.getValue()); // current value
                    logger.info("k {} v {}", key, value);
                }
            }
        });
    }
}
