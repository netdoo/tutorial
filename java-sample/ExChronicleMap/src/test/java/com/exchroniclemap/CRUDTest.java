package com.exchroniclemap;


import net.openhft.chronicle.map.ChronicleMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDTest {

    final static Logger logger = LoggerFactory.getLogger(CRUDTest.class);
    File file = new File("./map.dat");

    @Test
    public void _0_테스트_준비() {
        if (file.exists()) {
            file.delete();
        }

        file.deleteOnExit();
    }

    @Test
    public void _1_테스트_CRUD() {

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .entries(1_000L)
                .averageKeySize(100)
                .averageValueSize(100)
                .createOrRecoverPersistedTo(file)) {

            map.put("001", "RED");
            map.put("002", "BLUE");

            assertEquals("RED", map.get("001"));
            map.put("001", "LightRED");

            assertEquals("LightRED", map.get("001"));

            map.remove("001");
            assertEquals(null, map.get("001"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2_테스트_SIZE() {
        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .createPersistedTo(file)) {

            assertEquals(1, map.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _3_테스트_CLEAR() {

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .createPersistedTo(file)) {

            map.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _4_테스트_SIZE() {
        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .createPersistedTo(file)) {

            assertEquals(0, map.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _5_테스트_엔트리_증가() {

        logger.info("before file size {} (bytes)", file.length());

        try (ChronicleMap<String, String> map = ChronicleMap
                .of(String.class, String.class)
                .createPersistedTo(file)) {


            map.put("001", "MBC");
            map.put("002", "SBS");
            map.put("003", "KBS");
            map.put("004", "EBS");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _6_테스트_엔트리_증가이후_파일_사이즈() {
        logger.info("after file size {} (bytes)", file.length());
    }
}
