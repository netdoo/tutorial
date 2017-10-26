package com.exberkeleydb;

import com.exberkeleydb.domain.Box;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class EntrySample {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        Environment environment = null;
        Database database = null;
        Cursor cursor = null;

        try {
            EnvironmentConfig environmentConfig = new EnvironmentConfig();
            environmentConfig.setAllowCreate(true);
            environmentConfig.setTransactional(true);
            environment = new Environment(new File("C:\\temp\\dbEnv"), environmentConfig);

            // Open the database, creating one if it does not exist
            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.setAllowCreate(true);
            databaseConfig.setTransactional(true);

            database = environment.openDatabase(null, "TestDatabase", databaseConfig);

            StoredClassCatalog storedClassCatalog = new StoredClassCatalog(database);

            EntryBinding dataBinding = new SerialBinding(storedClassCatalog, Box.class);

            DatabaseEntry key = new DatabaseEntry();
            StringBinding.stringToEntry("myKey", key);

            Box box = new Box("1", "darkred");
            DatabaseEntry value = new DatabaseEntry();
            dataBinding.objectToEntry(box, value);

            if (database.put(null, key, value) == OperationStatus.SUCCESS) {
                System.out.println("success put");
            }

            if (database.get(null, key, value, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                String readKey = StringBinding.entryToString(key);
                Box readValue = (Box) dataBinding.entryToObject(value);
                System.out.println("success get key: '" + readKey + "' value: '" + readValue + "'.");
            } else {
                System.out.println("No record found with key '" + key + "'.");
            }

            cursor = database.openCursor(null, null);

            // Cursors returns records as pairs of DatabaseEntry objects
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            // Retrieve records with calls to getNext() until the
            // return status is not OperationStatus.SUCCESS
            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                String keyString = new String(foundKey.getData(), "UTF-8");
                String dataString = new String(foundData.getData(), "UTF-8");
                System.out.println("Key| Data : " + keyString + " | " +  dataString + "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            if (database != null) {
                database.close();
            }

            if (environment != null) {
                environment.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
