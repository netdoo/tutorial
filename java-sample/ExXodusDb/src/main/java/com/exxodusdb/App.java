package com.exxodusdb;


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

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);



    static void caseDuplicate(Environment env, Store store) {

        // put은 새로운 k, v 값으로 덮어써짐.
        // 3번째 입력된 k, v 값으로 덮어써짐.
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

        // add 는 기존 k 값이 있으면 false 반환.
        // 기존 k 값이 없으면 기록하고 true 반환
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull Transaction transaction) {
                boolean red = store.add(transaction, stringToEntry("Color"), stringToEntry("Red"));
                boolean green = store.add(transaction, stringToEntry("Color"), stringToEntry("Green"));
                boolean blue = store.add(transaction, stringToEntry("Color"), stringToEntry("Blue"));

                logger.info("red {} green {} blue {}", red, green, blue);

                ByteIterable color = store.get(transaction, stringToEntry("Color"));
                logger.info("save color is {}", entryToString(color));
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
                    for (int i = 0; i < partition; i++, offset.increment()) {

                        store.add(transaction, stringToEntry(keyPrefix + offset.getValue()), stringToEntry(valuePrefix + offset.getValue()));
                        /*
                        ByteIterable result = store.get(transaction, stringToEntry(key));

                        if (result == null) {
                            /// 기존에 입력된 키값이 없는 경우
                            store.put(transaction, stringToEntry(key), stringToEntry(val));
                        }
                        */
                    }
                }
            });

            if (offset.getValue() % 1_000_000 == 0) {
                logger.info("sum of put count {}", offset.getValue());
            }
        }
        stopWatch.stop();
        logger.info("finish bulkPut, elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));

        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("count {}", count);
    }

    public static void main( String[] args ) throws Exception {

        //cleanUp();


  /*      Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("Messages", StoreConfig.WITHOUT_DUPLICATES, txn));
*/
        //simple(env, store);
        //delete(env, store);
        //caseDuplicate(env, store);

        //bulkPut(env, store, 20_000, 10_000, "012345678901234567890123456789", "1");
//        env.close();
    }
}
