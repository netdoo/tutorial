package com.exsqlite;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LocalStorageSample {
    final static Logger logger = LoggerFactory.getLogger(LocalStorageSample.class);

    public void bulkInsertSample() throws Exception {
        LocalStorage storage = new LocalStorage("C:\\temp\\ls.db");
        long start = System.currentTimeMillis();
        storage.setAutoCommit(false);

        for (int i = 0; i < 100_000; i++) {
            storage.put(Integer.toString(i), "this is some text " + i);
        }

        storage.commit();
        logger.info("elpased time {}", System.currentTimeMillis() - start);
        storage.close();
    }

    public void putSample(LocalStorage storage) throws Exception {
        storage.put("1", "mbc");
        storage.put("2", "sbs");
        storage.put("3", "kbs");
    }

    public void putSampleByMap(LocalStorage storage) throws Exception {
        Map<String, String> kv = new HashMap<>();
        kv.put("4", "ebs");
        kv.put("5", "jtbc");
        kv.put("6", "tvn");

        storage.putAll(kv);
    }

    public void getSample(LocalStorage storage) throws Exception {
        logger.info("5 => {}", storage.get("5"));
    }

    public void getSomeSample(LocalStorage storage) throws Exception {
        Set<String> keys = new HashSet<>();
        keys.add("1");
        keys.add("3");
        keys.add("4");

        storage.get(keys).forEach((key, val) -> logger.info("key2 {} value {}", key, val));;
    }

    public void getAllSample(LocalStorage storage) throws Exception {
        Map<String, String> m = storage.getAll();
        m.forEach((key, val) -> logger.info("key {} value {}", key, val));
    }

    public void deleteSample(LocalStorage storage) throws Exception {
        storage.delete("5");
        logger.info("5 => {}", storage.get("5"));
    }

    public void deleteSomeSample(LocalStorage storage) throws Exception {
        Set<String> keys = new HashSet<>();
        keys.add("1");
        keys.add("2");
        storage.delete(keys);
    }

    public void deleteAllSample(LocalStorage storage) throws Exception {
        storage.deleteAll();
    }

    public void printAll(LocalStorage storage) throws Exception {
        storage.getAll().forEach((key, val) -> System.out.println(key + "," + val));
    }

    public void run() throws Exception {
        LocalStorage storage = new LocalStorage("C:\\temp\\ls.db");
        putSample(storage);
        putSampleByMap(storage);
        getSample(storage);
        getSomeSample(storage);
        getAllSample(storage);
        deleteSample(storage);
        printAll(storage);
        deleteAllSample(storage);
        storage.close();
    }
}
