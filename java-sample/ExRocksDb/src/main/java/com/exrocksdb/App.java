package com.exrocksdb;


import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.backup.BackupStrategy;
import jetbrains.exodus.env.*;
import jetbrains.exodus.management.Statistics;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.env.StoreConfig.WITHOUT_DUPLICATES;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    final static String dbPath = "C:\\Temp\\xodus.db";


    static void cleanUp() {
        File dir = new File(dbPath);

        if (dir.exists()) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
        } else {
            dir.mkdir();
        }
    }

    static void simple(Environment env, Store store) {

        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {
                store.put(transaction, stringToEntry("Hello"), stringToEntry("World!"));
            }
        });

        String value = env.computeInTransaction(new TransactionalComputable<String>() {
                            @Override
                            public String compute(@NotNull Transaction transaction) {
                                return entryToString(store.get(transaction, stringToEntry("Hello")));
                            }
                        });

        logger.info("value {}", value);

        String notExistValue = env.computeInTransaction(new TransactionalComputable<String>() {
            @Override
            public String compute(@NotNull Transaction transaction) {

                ByteIterable result = store.get(transaction, stringToEntry("Bye"));
                return (result == null) ? "" : entryToString(result);
            }
        });

        logger.info("notExistValue {}", notExistValue);
    }

    static void caseDuplicate(Environment env, Store store) {
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {
                store.put(transaction, stringToEntry("Hello"), stringToEntry("World!"));
                store.put(transaction, stringToEntry("Hello"), stringToEntry("World!!"));
                store.put(transaction, stringToEntry("Hello"), stringToEntry("World!!!"));

                if (store.get(transaction, stringToEntry("Hello")) != null) {
                    logger.info("hello is exist");
                }
            }
        });

        env.executeInTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    String key = entryToString(cursor.getKey());   // current key
                    String value = entryToString(cursor.getValue()); // current value
                    logger.info("k {} v {}", key, value);
                }
            }
        });
    }

    static void bulkPut(Environment env, Store store, final int loopCount, final int partition, String keyPrefix, String valuePrefix) {

        logger.info("start bulkPut");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int mainCount = loopCount / partition;
        MutableInt offset = new MutableInt(0);

        for (int i = 0; i < mainCount; i++) {
            env.executeInTransaction(new TransactionalExecutable() {
                @Override
                public void execute(@NotNull Transaction transaction) {
                    int idx;
                    String key, val;

                    for (int i = 0; i < partition; i++) {
                        idx = offset.incrementAndGet();
                        key = keyPrefix + idx;
                        val = valuePrefix + idx;

                        ByteIterable result = store.get(transaction, stringToEntry(key));

                        if (result == null) {
                            /// 기존에 입력된 키값이 없는 경우
                            store.put(transaction, stringToEntry(key), stringToEntry(val));
                        }
                    }
                }
            });

            if (offset.getValue() % 1_000_000 == 0) {
                logger.info("sum of put count {}", offset.getValue());
            }
        }
        stopWatch.stop();
        logger.info("finish bulkPut, elapsed time {} (secs)", stopWatch.getTime(TimeUnit.MINUTES));

        long count = env.computeInTransaction(txn -> store.count(txn));
        logger.info("count {}", count);
    }

    public static void main( String[] args ) throws Exception {

        cleanUp();

        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", WITHOUT_DUPLICATES, txn));

        //simple(env, store);
        //caseDuplicate(env, store);
        bulkPut(env, store, 20_000_000, 10_000, "012345678901234567890123456789", "1");

        env.close();
    }
}
