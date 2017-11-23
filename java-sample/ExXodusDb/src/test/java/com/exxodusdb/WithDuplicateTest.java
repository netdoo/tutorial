package com.exxodusdb;

import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

/**
 * Created by jhkwon78 on 2017-11-17.
 */
public class WithDuplicateTest {

    final Logger LOGGER = LoggerFactory.getLogger(WithDuplicateTest.class);
    final static String dbPath = "C:\\Temp\\xodus.db";


    class Entry {
        String key;
        String val;

        public Entry() {

        }

        public Entry(String key, String val) {
            this.key = key;
            this.val = val;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getVal() {
            return this.val;
        }
    }

    List<Entry> createDummyEntries() {
        List<Entry> entryList = new ArrayList<>();

        entryList.add(new Entry("MBC", "10"));
        entryList.add(new Entry("SBS", "6"));
        entryList.add(new Entry("KBS", "7"));
        entryList.add(new Entry("KBS", "9"));
        entryList.add(new Entry("EBS", "13"));

        return entryList;
    }

    // WITH_DUPLICATES 모드로 데이터베이스를 생성한 경우,
    // put 메서드는 중복된 키 값이 있어도 데이터가 추가되나,
    // add 메서드는 중복된 키 값이 있는 경우, 데이터가 추가가 안된다.

    @Test
    public void testWithDuplicatePutOp() throws Exception {
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore("Messages", StoreConfig.WITH_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            LOGGER.info("{}", store.put(txn, stringToEntry("MBC"), stringToEntry("10")));       // true
            LOGGER.info("{}", store.put(txn, stringToEntry("SBS"), stringToEntry("6")));        // true
            LOGGER.info("{}", store.put(txn, stringToEntry("KBS"), stringToEntry("7")));        // true
            LOGGER.info("{}", store.put(txn, stringToEntry("KBS"), stringToEntry("9")));        // true
            LOGGER.info("{}", store.put(txn, stringToEntry("EBS"), stringToEntry("13")));       // true
        });

        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        LOGGER.info("count {} ", count);

        // iterate
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    LOGGER.info("iter {} / {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });

        env.close();
    }

    @Test
    public void testWithDuplicateAddOp() throws Exception {
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore("Messages", StoreConfig.WITH_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            LOGGER.info("{}", store.add(txn, stringToEntry("MBC"), stringToEntry("10")));       // true
            LOGGER.info("{}", store.add(txn, stringToEntry("SBS"), stringToEntry("6")));        // true
            LOGGER.info("{}", store.add(txn, stringToEntry("KBS"), stringToEntry("7")));        // true
            LOGGER.info("{}", store.add(txn, stringToEntry("KBS"), stringToEntry("9")));        // false
            LOGGER.info("{}", store.add(txn, stringToEntry("EBS"), stringToEntry("13")));       // true
        });

        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        LOGGER.info("count {} ", count);

        // iterate
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    LOGGER.info("iter {} / {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });

        env.close();

        Pair<String, String> pair = new ImmutablePair<>("MBC", "10");

        //Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>("MBC", "10");

    }
}
