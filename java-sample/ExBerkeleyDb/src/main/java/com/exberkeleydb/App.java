package com.exberkeleydb;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;

import java.io.File;


public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);


    public static void main( String[] args ) {

        Environment myDbEnvironment = null;
        Database myDatabase = null;
        String key = "myKey";
        String data = "myData";
        Cursor myCursor = null;

        try {
            // Open the environment, creating one if it does not exist
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            envConfig.setTransactional(true);
            myDbEnvironment = new Environment(new File("C:\\temp\\dbEnv"),
                    envConfig);

            // Open the database, creating one if it does not exist
            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            dbConfig.setTransactional(true);

            //dbConfig.setType(DatabaseType.BTREE);
            //dbConfig.setSortedDuplicates(true);
            //dbConfig.setReadUncommitted(true);

            myDatabase = myDbEnvironment.openDatabase(null,
                    "TestDatabase", dbConfig);


            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
            DatabaseEntry theData = new DatabaseEntry(data.getBytes("UTF-8"));
            myDatabase.put(null, theKey, theData);


//            EntryBinding dataBinding = new SerialBinding(scc, String.class);


            // Call get() to query the database
            if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
                    OperationStatus.SUCCESS) {

                // Translate theData into a String.
                byte[] retData = theData.getData();
                String foundData = new String(retData, "UTF-8");
                System.out.println("key: '" + key + "' data: '" + foundData + "'.");
            } else {
                System.out.println("No record found with key '" + key + "'.");
            }



            // Delete the entry (or entries) with the given key
            myDatabase.delete(null, theKey);



            Transaction txn = null;
            // Get a transaction
            txn = myDbEnvironment.beginTransaction(null, null);
            // Perform 50 transactions
            for (int i=0; i<50; i++) {
                // Do the put
                StringBinding.stringToEntry("k"+i, theKey);
                StringBinding.stringToEntry("v"+i, theData);
                myDatabase.put(txn, theKey, theData);
            }


            txn.commit();


                myCursor = myDatabase.openCursor(null, null);

            // Cursors returns records as pairs of DatabaseEntry objects
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            // Retrieve records with calls to getNext() until the
            // return status is not OperationStatus.SUCCESS
            while (myCursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
                    OperationStatus.SUCCESS) {
                String keyString = new String(foundKey.getData(), "UTF-8");
                String dataString = new String(foundData.getData(), "UTF-8");
                System.out.println("Key| Data : " + keyString + " | " +
                        dataString + "");
            }


        } catch (Exception e) {
            //  Exception handling
            System.out.println(e);
        }

        try {
            if (myCursor != null) {
                myCursor.close();
            }

            if (myDatabase != null) {
                myDatabase.close();
            }

            if (myDbEnvironment != null) {
                myDbEnvironment.close();
            }
        }catch (Exception e) {

        }

    }
}
