package com.exroundrobinhashfile;

import java.util.HashMap;
import java.util.Map;

public class BufferedBerkeleyDB implements AutoCloseable {
    BerkeleyDB berkeleyDB;
    Map<String, String> bufferedMap = new HashMap<>();
    int bufferSize = 3;

    public BufferedBerkeleyDB(BerkeleyDB berkeleyDB, int bufferSize) {
        this.berkeleyDB = berkeleyDB;
        this.bufferSize = bufferSize;
    }

    public void put(String key, String value) throws Exception {
        this.bufferedMap.put(key, value);
        if (this.bufferedMap.size() > this.bufferSize) {
            flush();
        }
    }

    public String get(String key) throws Exception {
        if (this.bufferedMap.size() > 0) {
            flush();
        }

        return this.berkeleyDB.get(key);
    }

    public boolean moveFirst() throws Exception {
        flush();
        return this.berkeleyDB.moveFirst();
    }

    public BerkeleyDB.Entry getNext() throws Exception {
        return this.berkeleyDB.getNext();
    }

    void flush() throws Exception {
        berkeleyDB.putAll(this.bufferedMap);
        this.bufferedMap.clear();
    }

    @Override
    public void close() throws Exception {
        flush();
        berkeleyDB.close();
    }
}
