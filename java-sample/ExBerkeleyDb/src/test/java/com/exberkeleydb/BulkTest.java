package com.exberkeleydb;

import com.exberkeleydb.domain.Box;
import com.sleepycat.je.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);
    final static File homeDir = new File("./dbhome");
    final static String dbName = "testDB";
    final static long MAX_PUT_COUNT = 100_000_000L;

    @Test
    public void _0_테스트_준비() {
        if (homeDir.exists()) {
            Arrays.stream(homeDir.listFiles()).forEach(File::delete);
        } else {
            homeDir.mkdir();
        }
    }

    @Test
    public void _1_벌크_테스트() {
        Environment environment = null;
        Database database = null;
        Cursor cursor = null;
        Transaction transaction;
        StopWatch stopWatch = new StopWatch();

        try {
            EnvironmentConfig environmentConfig = new EnvironmentConfig();
            environmentConfig.setAllowCreate(true);
            environmentConfig.setTransactional(true);
            environment = new Environment(homeDir, environmentConfig);

            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.setAllowCreate(true);
            databaseConfig.setTransactional(true);

            database = environment.openDatabase(null, dbName, databaseConfig);

            StringBuilder builder = new StringBuilder();

            while (builder.length() < 4096) {
                builder.append("0123456789");
            }

            String dummyValue = builder.toString();
            String dummyKey = dummyValue.substring(0, 512);

            for (long i = 0; i < MAX_PUT_COUNT; i++) {
                transaction = environment.beginTransaction(null, null);

                for (int j = 0; j < 100_000; j++, i++) {
                    String key = dummyKey + i;
                    database.put(transaction, new DatabaseEntry(key.getBytes("UTF-8")),
                            new DatabaseEntry(dummyValue.getBytes("UTF-8")));
                }

                i--;
                transaction.commitSync();
                logger.info("put {}", i);
            }

        } catch (Exception e) {
            logger.info("", e);
        }

        try {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }

            if (environment != null) {
                environment.close();
            }
        } catch (Exception e) {
            logger.info("", e);
        }
    }
}
