package com.exsqlite;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SQLite3 {

    String dbPath;
    Connection connection;

    public boolean open(String dbPath) {

        if (isOpen()) {
            close();
        }

        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            this.dbPath = dbPath;
            return true;
        } catch (SQLException e) {}

        return false;
    }

    public boolean isOpen() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDbPath() {
        return this.dbPath;
    }

    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException ex) {}

        this.dbPath = "";
    }

    /// create, drop
    public int executeSQL(String sql) throws Exception {
        PreparedStatement ps = this.connection.prepareStatement(sql);
        return ps.executeUpdate();
    }

    /// insert, update, delete
    public int executeSQL(String sql, Object... params) throws Exception {
        PreparedStatement ps = this.connection.prepareStatement(sql);
        return setPreparedStatementParams(ps, params).executeUpdate();
    }

    PreparedStatement setPreparedStatementParams(PreparedStatement ps, Object... params) throws Exception {
        int no = 1;

        for (Object o : params) {
            if (o instanceof Collection) {
                for (Object co : (Collection)o) {
                    setPreparedStatementParam(ps, no++, co);
                }
            } else {
                setPreparedStatementParam(ps, no++, o);
            }
        }

        return ps;
    }

    public PreparedStatement setPreparedStatementParam(PreparedStatement ps, int no, Object o) throws Exception {

        if (o instanceof String) {
            ps.setString(no, (String)o);
        } else if (o instanceof Double) {
            ps.setDouble(no, ((Double)o).doubleValue());
        } else if (o instanceof Long) {
            ps.setLong(no, ((Long)o).longValue());
        } else if (o instanceof Integer) {
            ps.setInt(no, ((Integer)o).intValue());
        } else if (o instanceof byte[]) {
            ps.setBytes(no, (byte[]) o);
        }

        return ps;
    }

    public ResultSet select(String sql) throws Exception {
        PreparedStatement ps = this.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet select(String sql, Object... params) throws Exception {
        PreparedStatement ps = this.connection.prepareStatement(sql);
        return setPreparedStatementParams(ps, params).executeQuery();
    }

    public void setAutoCommit(boolean autoCommit) throws Exception{
        this.connection.setAutoCommit(autoCommit);
    }

    public void commit() throws Exception {
        this.connection.commit();
    }
}
