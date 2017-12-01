package com.exberkeleydb;

import com.sun.jndi.ldap.Ber;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);
    String dbDir = "./dbHome";

    @Test
    public void _0_테스트_준비() {

    }

    @Test
    public void _1_기본_테스트() throws Exception {

        BerkeleyDB berkeleyDB = new BerkeleyDB();
        berkeleyDB.open(dbDir, "mydb", false);
        berkeleyDB.put("001", "RED");
        berkeleyDB.put("002", "GREEN");
        berkeleyDB.put("003", "BLUE");

        Map<String, String> colors = new HashMap<>();
        colors.put("004", "BLACK");
        colors.put("005", "WHITE");
        colors.put("006", "PINK");

        berkeleyDB.putAll(colors);

        berkeleyDB.moveFirst();
        BerkeleyDB.Entry entry;

        while ( (entry = berkeleyDB.getNext()) != null) {
            logger.info("{} : {}", entry.getKey(), entry.getValue());
        }

        assertEquals("RED", berkeleyDB.get("001"));
        assertEquals("GREEN", berkeleyDB.get("002"));
        assertEquals("BLUE", berkeleyDB.get("003"));
        assertEquals("BLACK", berkeleyDB.get("004"));
        assertEquals("WHITE", berkeleyDB.get("005"));
        assertEquals("PINK", berkeleyDB.get("006"));

        berkeleyDB.close();
    }

    @Test
    public void _2_기본_테스트_DeleteOnExit() throws Exception {

        BerkeleyDB berkeleyDB = new BerkeleyDB();
        berkeleyDB.open(dbDir, "mydb", true);
        berkeleyDB.put("001", "RED");
        berkeleyDB.put("002", "GREEN");
        berkeleyDB.put("003", "BLUE");

        Map<String, String> colors = new HashMap<>();
        colors.put("004", "BLACK");
        colors.put("005", "WHITE");
        colors.put("006", "PINK");

        berkeleyDB.putAll(colors);

        berkeleyDB.moveFirst();
        BerkeleyDB.Entry entry;

        while ( (entry = berkeleyDB.getNext()) != null) {
            logger.info("{} : {}", entry.getKey(), entry.getValue());
        }

        assertEquals("RED", berkeleyDB.get("001"));
        assertEquals("GREEN", berkeleyDB.get("002"));
        assertEquals("BLUE", berkeleyDB.get("003"));
        assertEquals("BLACK", berkeleyDB.get("004"));
        assertEquals("WHITE", berkeleyDB.get("005"));
        assertEquals("PINK", berkeleyDB.get("006"));

        berkeleyDB.close();
    }
}
