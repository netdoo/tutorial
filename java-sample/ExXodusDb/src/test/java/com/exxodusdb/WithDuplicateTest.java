package com.exxodusdb;

import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WithDuplicateTest {

    final Logger logger = LoggerFactory.getLogger(WithDuplicateTest.class);

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

    @Test
    public void _0_테스트_준비() throws Exception {
        TestEnv.cleanUp();
    }

    @Test
    public void _1_중복허용_PUT_테스트_준비() throws Exception {
        TestEnv.cleanUp();
    }

    // WITH_DUPLICATES 모드로 데이터베이스를 생성한 경우,
    // put 메서드는 중복된 키 값이 있어도 데이터가 추가된다.

    @Test
    public void _2_중복허용_PUT_테스트() throws Exception {
        Environment env = Environments.newInstance(TestEnv.dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore(TestEnv.storeName, StoreConfig.WITH_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            createDummyEntries().forEach(entry -> {
                logger.info("{}/{} {}", entry.getKey(), entry.getVal(), store.put(txn, stringToEntry(entry.getKey()), stringToEntry(entry.getVal())));
            });
        });

        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("count {} ", count);

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

    @Test
    public void _3_중복허용_ADD_테스트_준비() throws Exception {
        TestEnv.cleanUp();
    }

    // WITH_DUPLICATES 모드로 데이터베이스를 생성한 경우,
    // add 메서드는 중복된 키 값이 있는 경우, 데이터가 추가가 안된다.

    @Test
    public void _4_중복허용_ADD_테스트() throws Exception {

        Environment env = Environments.newInstance(TestEnv.dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore(TestEnv.storeName, StoreConfig.WITH_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            createDummyEntries().forEach(entry -> {
                logger.info("{}/{} {}", entry.getKey(), entry.getVal(), store.add(txn, stringToEntry(entry.getKey()), stringToEntry(entry.getVal())));
            });
        });

        long count = env.computeInReadonlyTransaction(txn -> store.count(txn));
        logger.info("count {} ", count);

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
