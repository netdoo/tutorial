package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class MultiStore {
    final static Logger logger = LoggerFactory.getLogger(MultiStore.class);
    final static String dbPath = "C:\\Temp\\xodus\\multi_store";

    static void print(Environment env, Store store) {
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    logger.info("key [{}] value [{}]", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });
    }

    static void print(Map<String, String> raw) {
        raw.forEach((k, v) -> {
            logger.info("key [{}] value [{}]", k, v);
        });
    }

    public static void main( String[] args ) throws Exception {

        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store raw = env.computeInTransaction(txn ->
                env.openStore("raw", StoreConfig.WITHOUT_DUPLICATES, txn));

        Store all_name_price = env.computeInTransaction(txn ->
                env.openStore("all_name_price", StoreConfig.WITHOUT_DUPLICATES, txn));

        Store all_id = env.computeInTransaction(txn ->
                env.openStore("all_id", StoreConfig.WITHOUT_DUPLICATES, txn));

        env.executeInTransaction(txn -> {
            raw.put(txn, stringToEntry("1"), stringToEntry("1\tRed\t1000"));
            raw.put(txn, stringToEntry("2"), stringToEntry("2\tGreen\t1000"));
            boolean result_1 = raw.put(txn, stringToEntry("3"), stringToEntry("3\tBlue\t1000"));
            boolean result_2 = raw.put(txn, stringToEntry("3"), stringToEntry("3\tBlue\t2000"));
            raw.put(txn, stringToEntry("4"), stringToEntry("4\tBlue\t2000"));
        });

        logger.info("=============== raw ===============");

        print(env, raw);

        env.executeInTransaction(txn -> {
            try (Cursor cursor = raw.openCursor(txn)) {
                while (cursor.getNext()) {
                    String value = entryToString(cursor.getValue());
                    String cols[] = value.split("\t");
                    String key = cols[1] + "." + cols[2];
                    all_name_price.put(txn, stringToEntry(key), cursor.getValue());
                }
            }
        });

        env.executeInTransaction(txn -> {
            try (Cursor cursor = all_name_price.openCursor(txn)) {
                while (cursor.getNext()) {
                    String value = entryToString(cursor.getValue());
                    String cols[] = value.split("\t");
                    String key = cols[0];
                    all_id.put(txn, stringToEntry(key), cursor.getValue());
                }
            }
        });

        logger.info("=============== all_name_price ===============");

        print(env, all_name_price);

        logger.info("=============== all_id ===============");

        print(env, all_id);

        ////////////////////////////////////////////////////////////////////////////
        logger.info("=============== part ===============");

        Map<String, String> partRawData = new HashMap<>();
        partRawData.put("1", "1\tRed\t1000");
        partRawData.put("1", "1\tRed\t900");
        partRawData.put("5", "5\tGreen\t1000");
        partRawData.put("6", "6\tSky\t2000");

        print(partRawData);

        env.executeInTransaction(txn -> {
            partRawData.forEach((key, value) -> {
                ByteIterable existIdValue = all_id.get(txn, stringToEntry(key));

                if (existIdValue != null) {
                    // 동일한 ID가 존재하는 경우
                    logger.info("동일한 ID가 존재하는 경우 과거[{}] 현재[{}]", entryToString(existIdValue), value);
                }

                String cols[] = value.split("\t");
                String secondKey = cols[1] + "." + cols[2];
                ByteIterable existNamePriceValue = all_name_price.get(txn, stringToEntry(secondKey));

                if (existNamePriceValue != null) {
                    // 동일한 상품명+가격이 존재하는 경우
                    logger.info("동일한 상품명+가격이 존재하는 경우 과거[{}] 현재[{}]", entryToString(existNamePriceValue), value);
                }

                if (existIdValue == null && existNamePriceValue == null) {
                    logger.info("동일한 ID도 없고, 동일한 상품명+가격도 없는 경우 현재[{}]", value);
                }
            });
        });

        env.close();

    }
}

