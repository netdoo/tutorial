package com.exberkeleydb;

import com.exberkeleydb.domain.Box;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.*;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.invoke.util.VerifyAccess;
import sun.util.locale.provider.TimeZoneNameUtility;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    final static File homeDir = new File("C:\\temp\\dbEnv");
    final static String dbName = "testDB";
    final static int MAX_COUNT = 1;

    static void cleanUp(File homeDir) {
        if (homeDir.exists()) {
            Arrays.stream(homeDir.listFiles()).forEach(File::delete);
        } else {
            homeDir.mkdir();
        }
    }

    static Environment createEnvironment(File homeDir) throws Exception {
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setAllowCreate(true);
        environmentConfig.setTransactional(true);
        return new Environment(homeDir, environmentConfig);
    }

    static Database createDatabase(Environment environment, String dbName) throws Exception {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setAllowCreate(true);
        databaseConfig.setTransactional(true);

        return environment.openDatabase(null, dbName, databaseConfig);
    }

    static OperationStatus put(Database database, String key, Box value) throws Exception {
        return database.put(null, new DatabaseEntry(key.getBytes("UTF-8")),
                new DatabaseEntry(SerializationUtils.serialize(value)));
    }

    static OperationStatus put(Database database, String key, String value) throws Exception {
        return database.put(null, new DatabaseEntry(key.getBytes("UTF-8")),
                new DatabaseEntry(value.getBytes("UTF-8")));
    }

    static OperationStatus put(Database database, Transaction transaction, String key, Box value) throws Exception {
        return database.put(transaction, new DatabaseEntry(key.getBytes("UTF-8")),
                new DatabaseEntry(SerializationUtils.serialize(value)));
    }

    static OperationStatus del(Database database, String key) throws Exception {
        return database.delete(null, new DatabaseEntry(key.getBytes("UTF-8")));
    }

    static Box get(Database database, String key) throws Exception {
        DatabaseEntry value = new DatabaseEntry();

        if (database.get(null, new DatabaseEntry(key.getBytes("UTF-8")), value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            return (Box)SerializationUtils.deserialize(value.getData());
        }

        return new Box();
    }

    static boolean exist(Database database, String key) throws Exception {
        DatabaseEntry value = new DatabaseEntry();
        return (database.get(null, new DatabaseEntry(key.getBytes("UTF-8")), value, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    }

    static void printAll(Cursor cursor) throws Exception {
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();

        // Moves the cursor to the first key/data pair of the database.
        if (cursor.getFirst(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            do {
                logger.info("{} / {}", new String(key.getData(), "UTF-8"), SerializationUtils.deserialize(value.getData()));
            } while (cursor.getNext(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS);
        }
    }

    static void reversePrintAll(Cursor cursor) throws Exception {
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();

        // Moves the cursor to the first key/data pair of the database.
        if (cursor.getLast(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            do {
                logger.info("{} / {}", new String(key.getData(), "UTF-8"), SerializationUtils.deserialize(value.getData()));
            } while (cursor.getPrev(key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS);
        }
    }

    public static void main( String[] args ) throws Exception {
        Environment environment = null;
        Database database = null;
        Cursor cursor = null;
        Transaction transaction;
        StopWatch stopWatch = new StopWatch();

        try {
            cleanUp(homeDir);
            environment = createEnvironment(homeDir);
            database = createDatabase(environment, dbName);

            logger.info("=== put ===");
            put(database, "1", new Box("a01", "red"));
            put(database, "2", new Box("a02", "green"));
            put(database, "3", new Box("a03", "blue"));

            logger.info("=== get ===");
            logger.info("{}", get(database, "1"));
            logger.info("{}", get(database, "2"));
            logger.info("{}", get(database, "3"));

            logger.info("=== update ===");
            logger.info("{}", put(database, "1", new Box("a01", "darkred")));

            logger.info("=== delete ===");
            logger.info("{}", del(database, "2"));

            logger.info("=== count ===");
            logger.info("{}", database.count());

            logger.info("=== iterate ===");
            CursorConfig cursorConfig = new CursorConfig();
            cursorConfig.setReadUncommitted(true);
            cursor = database.openCursor(null, cursorConfig);
            printAll(cursor);

            logger.info("=== reverse iterate ===");
            reversePrintAll(cursor);

            logger.info("=== transaction commit ===");

            transaction = environment.beginTransaction(null, null);
            put(database, transaction, "trans1", new Box("trans1", "white"));
            transaction.commitSync();

            logger.info("=== transaction abort ===");
            transaction = environment.beginTransaction(null, null);
            put(database, transaction, "trans2", new Box("trans2", "black"));
            transaction.abort();

            printAll(cursor);



            logger.info("=== bulk put (slow) ===");
            stopWatch.start();

            for (int i = 0; i < MAX_COUNT; i++) {
                put(database, "slow"+i, new Box("slow"+i, String.valueOf(i)));
            }

            stopWatch.stop();
            logger.info("bulk put (slow) elapsed time {} (ms)", stopWatch.getTime(TimeUnit.MILLISECONDS));

            logger.info("=== bulk put (fast)");

            stopWatch.reset();
            stopWatch.start();

            transaction = environment.beginTransaction(null, null);

            for (int i = 0; i < MAX_COUNT; i++) {
                put(database, transaction, "fast"+i, new Box("fast"+i, String.valueOf(i)));
            }

            transaction.commitSync();
            stopWatch.stop();
            logger.info("bulk put (fast) elapsed time {} (ms)", stopWatch.getTime(TimeUnit.MILLISECONDS));

            printAll(cursor);

            logger.info("=== bulk put with transaction (fast)");

            transaction = environment.beginTransaction(null, null);

            put(database, transaction, "LightRed", new Box("111", "LightRed"));
            put(database, transaction, "LightRed", new Box("1,111", "LightRed"));
            put(database, transaction, "LightRed", new Box("11,111", "LightRed"));
            put(database, transaction, "LightRed", new Box("111,111", "LightRed"));

            transaction.commitSync();

            printAll(cursor);

            logger.info("exist LightRed {}", exist(database, "LightRed"));
            logger.info("exist DarkRed {}", exist(database, "DarkRed"));

            /*
            logger.info("=== heavy bulk put (slow) ===");
            stopWatch.reset();
            stopWatch.start();

            TransactionConfig transactionConfig = new TransactionConfig();
            transactionConfig.setSync(false);
            transactionConfig.setWriteNoSync(true);
            transaction = environment.beginTransaction(null, transactionConfig);

            for (int i = 0; i < 1_800_000; i++) {
                put(database, "heavy"+i, "heavy"+i);
                if (i % 100_000 == 0) {
                    logger.info("insert {}", i);
                }
            }
            transaction.commitSync();
            stopWatch.stop();
            logger.info("heavy bulk put (slow) elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
            */

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
