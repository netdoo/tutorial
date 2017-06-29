package com.exsqlite;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LocalStorage extends SQLite3 {
    final String LocalStorageScheme = "CREATE TABLE IF NOT EXISTS LocalStorage(_key TEXT PRIMARY KEY, _val TEXT);";

    public LocalStorage(String dbPath) throws Exception {
        open(dbPath);
        executeSQL(LocalStorageScheme);
    }

    public synchronized void put(String key, String value) throws Exception {
        executeSQL("INSERT OR REPLACE INTO LocalStorage (_key, _val) VALUES (?, ?)", key, value);
    }

    public synchronized void putAll(Map<String, String> param) throws Exception {
        setAutoCommit(false);
        Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            put(entry.getKey(), entry.getValue());
        }

        commit();
        setAutoCommit(true);
    }

    public String get(String key) throws Exception {
        ResultSet rs = select("SELECT _val FROM LocalStorage WHERE _key = ?", key);
        return (rs.next()) ? rs.getString("_val") : "";
    }

    public Map<String, String> get(Set<String> keys) throws Exception {
        return resultSetToMap(select(createBulkSQL("SELECT _key, _val", keys), keys));
    }

    public Map<String, String> getAll() throws Exception {
        return resultSetToMap(select("SELECT _key, _val FROM LocalStorage"));
    }

    public synchronized void delete(String key) throws Exception {
        executeSQL("DELETE FROM LocalStorage WHERE _key = ?", key);
    }

    public synchronized void delete(Set<String> keys) throws Exception {
        executeSQL(createBulkSQL("DELETE", keys), keys);
    }

    public synchronized void deleteAll() throws Exception {
        executeSQL("DELETE FROM LocalStorage");
    }

    private Map<String, String> resultSetToMap(ResultSet rs) throws Exception {
        Map<String, String> m = new HashMap<>();

        while (rs.next()) {
            m.put(rs.getString("_key"), rs.getString("_val"));
        }

        return m;
    }

    private String createBulkSQL(String headSQL, Set<String> keys) {
        StringBuilder builder = new StringBuilder();
        builder.append(headSQL)
               .append(" FROM LocalStorage WHERE _key in (");
        keys.stream().forEach(key -> builder.append("?,"));
        return builder.deleteCharAt(builder.length()-1).append(')').toString();
    }
}
