package com.exhikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("1111");
        hikariConfig.setMaximumPoolSize(100);
        hikariConfig.setAutoCommit(true);
        hikariConfig.setConnectionTimeout(60_000);

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Connection connection = ds.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test.memo");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            logger.info("{} {}", rs.getInt(1), rs.getString(2));
        }

        rs.close();
        preparedStatement.close();
        connection.close();
        ds.close();
    }
}

