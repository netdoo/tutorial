package com.exberkeleydb;

import com.exberkeleydb.domain.Box;
import com.sleepycat.je.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BerkeleyDB {
    File dbDirFile;

    String dbDir;
    String dbName;

    boolean isOpen;
    Environment environment = null;
    Database database = null;
    Cursor cursor = null;
    Transaction transaction;

    DatabaseEntry key = new DatabaseEntry();
    DatabaseEntry value = new DatabaseEntry();

    boolean deleteOnExit;

    static class Entry {
        String key;
        String value;

        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }

    public BerkeleyDB() {
    }

    public void open(String dbDir, String dbName, boolean deleteOnExit) throws Exception {
        this.dbDir = dbDir;
        this.dbName = dbName;
        this.isOpen = true;
        this.deleteOnExit = deleteOnExit;

        File dir = new File(this.dbDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setAllowCreate(true);
        environmentConfig.setTransactional(true);
        this.environment = new Environment(dir, environmentConfig);

        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setAllowCreate(true);
        databaseConfig.setTransactional(true);

        this.database = this.environment.openDatabase(null, dbName, databaseConfig);


        CursorConfig cursorConfig = new CursorConfig();
        cursorConfig.setReadUncommitted(true);
        this.cursor = database.openCursor(null, cursorConfig);


    }

    public void close() throws Exception {
        if (cursor != null) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }

        if (environment != null) {
            environment.close();
        }

        this.isOpen = false;

        if (deleteOnExit) {
            File dir = new File(this.dbDir);

            if (dir.exists()) {
                Arrays.stream(dir.listFiles()).forEach(File::delete);
            } else {
                dir.mkdir();
            }
        }
    }

    public void put(String key, String value) throws Exception {
        this.database.put(transaction, new DatabaseEntry(key.getBytes("UTF-8")),
                new DatabaseEntry(value.getBytes("UTF-8")));
    }

    public void putAll(Map<String, String> m) throws Exception {
        this.transaction = this.environment.beginTransaction(null, null);

        for (Map.Entry<String, String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }

        transaction.commitSync();
    }

    public String get(String key) throws Exception {
        DatabaseEntry value = new DatabaseEntry();

        if (this.database.get(null, new DatabaseEntry(key.getBytes("UTF-8")), value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            return new String(value.getData(), "UTF-8");
        }

        return null;
    }

    public boolean moveFirst() throws Exception {
        return this.cursor.getFirst(this.key, this.value, LockMode.DEFAULT) == OperationStatus.SUCCESS;
    }

    public Entry getNext() throws Exception {

        if (this.key.getSize() == 0) {
            return null;
        }

        Entry entry = new Entry(new String(this.key.getData(), "UTF-8"), new String(this.value.getData(), "UTF-8"));

        if (this.cursor.getNext(this.key, this.value, LockMode.DEFAULT) != OperationStatus.SUCCESS) {
            this.key.setSize(0);
        }

        return entry;
    }
}
