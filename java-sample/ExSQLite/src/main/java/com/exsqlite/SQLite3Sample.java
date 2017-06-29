package com.exsqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;

public class SQLite3Sample {
    final static Logger logger = LoggerFactory.getLogger(SQLite3Sample.class);
    public void run() {
        int ret = 0;
        String dbPath = "C:\\temp\\test.db";
        SQLite3 sqLite3 = new SQLite3();
        sqLite3.open(dbPath);
        sqLite3.open(dbPath);

        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real,\n"
                + "	logo blob\n"
                + ");";

        try {
            sqLite3.executeSQL(sql);
        } catch (Exception e) {
            logger.info("fail to executeQuery {}", e.getMessage());
        }

        try {
            sqLite3.executeSQL("DELETE FROM warehouses");
        } catch(Exception e) {
            logger.info("fail to delete {}", e.getMessage());
        }

        try {
            String insertSQL = "INSERT INTO warehouses (name, capacity) VALUES (?, ?)";
            ret = sqLite3.executeSQL(insertSQL, String.valueOf("MBC"), Double.valueOf(3000));
            logger.info("ret {}", ret);

            ret = sqLite3.executeSQL(insertSQL, String.valueOf("KBS"), Double.valueOf(4000));
            logger.info("ret {}", ret);

            ret = sqLite3.executeSQL(insertSQL, String.valueOf("SBS"), Double.valueOf(5000));
            logger.info("ret {}", ret);

            insertSQL = "INSERT INTO warehouses (name, capacity, logo) VALUES (?, ?, ?)";
            byte[] logoBytes = Files.readAllBytes(Paths.get("C:\\temp\\iu.png"));                     ;
            ret = sqLite3.executeSQL(insertSQL, String.valueOf("LOEN"), Double.valueOf(7000), logoBytes);

        } catch (Exception e) {
            logger.info("fail to insert {}", e.getMessage());
        }

        try {
            String selectSQL =  "SELECT id, name, capacity, logo FROM warehouses";
            ResultSet rs = sqLite3.select(selectSQL);
            while (rs.next()) {
                logger.info("id {} name {} capacity {}", rs.getInt("id"), rs.getString("name"), rs.getDouble("capacity"));
                InputStream is = rs.getBinaryStream("logo");
                if (null != is) {
                    Files.copy(is, Paths.get("C:\\temp\\logo.png"));
                }
            }
        } catch(Exception e) {
            logger.info("fail to select {}", e.getMessage());
        }

        try {
            String updateSQL = "UPDATE warehouses SET name = ?, capacity = ? WHERE id = ?";
            ret = sqLite3.executeSQL(updateSQL, String.valueOf("SBS TV"), Double.valueOf(5500), Integer.valueOf(3));
            logger.info("update ret {}", ret);

        } catch (Exception e) {
            logger.info("fail to update {}", e.getMessage());
        }

        try {
            long start = System.currentTimeMillis();
            sqLite3.setAutoCommit(false);
            String bulkInsertSQL = "INSERT INTO warehouses (name, capacity) VALUES (?, ?)";
            for (int i = 0; i < 50000; i++) {
                ret = sqLite3.executeSQL(bulkInsertSQL, String.valueOf("SBS " + i), Double.valueOf(5000 + i));
            }

            sqLite3.commit();
            logger.info("elapsed time {} (msec)", System.currentTimeMillis() - start);

        } catch (Exception e) {
            logger.info("fail to bulk insert {}", e.getMessage());
        }

        sqLite3.close();
        sqLite3.close();
    }
}
