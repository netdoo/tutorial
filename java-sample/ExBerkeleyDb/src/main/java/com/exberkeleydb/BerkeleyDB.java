package com.exberkeleydb;

import com.sleepycat.je.*;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class BerkeleyDB implements AutoCloseable {

    final static String CHARSET = "UTF-8";

    String dbDir;
    String dbName;

    Environment environment;
    Database database;
    Cursor cursor;
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

    public BerkeleyDB(String dbDir, String dbName, boolean deleteOnExit) throws Exception {
        this.dbDir = dbDir;
        this.dbName = dbName;
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

    @Override
    public void close() throws Exception {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        if (database != null) {
            database.close();
            database = null;
        }

        if (environment != null) {
            environment.close();
            environment = null;
        }

        if (deleteOnExit) {
            File dir = new File(this.dbDir);

            if (dir.exists()) {
                Arrays.stream(dir.listFiles()).forEach(File::delete);
            } else {
                dir.mkdir();
            }
        }
    }

    public void beginTransaction() throws Exception {
        this.transaction = this.environment.beginTransaction(null, null);
    }

    public void commitTransaction() throws Exception {
        this.transaction.commitSync();
    }

    public void abortTransaction() throws Exception {
        this.transaction.abort();
    }

    public void put(String key, String value) throws Exception {
        this.database.put(transaction, new DatabaseEntry(key.getBytes(CHARSET)),
                new DatabaseEntry(value.getBytes(CHARSET)));
    }

    public void putAll(Map<String, String> m) throws Exception {

        beginTransaction();

        for (Map.Entry<String, String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }

        commitTransaction();
    }

    public String get(String key) throws Exception {
        DatabaseEntry value = new DatabaseEntry();

        if (this.database.get(null, new DatabaseEntry(key.getBytes(CHARSET)), value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            return new String(value.getData(), CHARSET);
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

        Entry entry = new Entry(new String(this.key.getData(), CHARSET), new String(this.value.getData(), CHARSET));

        if (this.cursor.getNext(this.key, this.value, LockMode.DEFAULT) != OperationStatus.SUCCESS) {
            this.key.setSize(0);
        }

        return entry;
    }

    public long count() throws Exception {
        return this.database.count();
    }
}
