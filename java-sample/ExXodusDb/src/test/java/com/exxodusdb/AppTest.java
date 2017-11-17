package com.exxodusdb;

import com.exxodusdb.domain.EPFile;
import com.exxodusdb.domain.EPFileLine;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);
    String dbDirPath = "C:\\temp\\EP\\test\\db\\";

    String inputAllDirPath = "C:\\temp\\EP\\test\\all\\input\\";
    String outputAllDirPath = "C:\\temp\\EP\\test\\all\\output\\";
    String appendAllPath = outputAllDirPath + "ep_all_result.txt";

    String inputUpdateDirPath = "C:\\temp\\EP\\test\\update\\input\\";
    String outputUpdateDirPath = "C:\\temp\\EP\\test\\update\\output\\";
    String appendUpdatePath = outputUpdateDirPath + "ep_update_result.txt";

    public boolean verifyDbData(String dbPath, Map<String, String> verifyMap) {
        Environment env = Environments.newInstance(dbPath);
        Store store = env.computeInTransaction(txn -> env.openStore("EP", StoreConfig.WITHOUT_DUPLICATES, txn));
        AtomicInteger errorCount = new AtomicInteger();

        env.executeInReadonlyTransaction(txn -> {
            verifyMap.forEach((key, value) -> {
                ByteIterable exist = store.get(txn, stringToEntry(key));
                if (exist == null) {
                    errorCount.incrementAndGet();
                    LOGGER.error("No such {}", key);
                }
            });
        });

        env.close();

        if (errorCount.get() == 0) {
            LOGGER.info("데이터 검증 성공");
            return true;
        }

        LOGGER.error("데이터 검증 실패 {} 건이 다름.", errorCount.get());
        return false;
    }

    @Test
    public void test01EpAll() throws Exception {

        boolean isAllEP = true;

        // 1. 더미 파일 생성
        EPFile epFile = new EPFile(isAllEP);
        epFile.add(new EPFileLine("1", "RED", "1000"));
        epFile.add(new EPFileLine("2", "RED", "2000"));
        epFile.add(new EPFileLine("3", "BLUE", "2000"));
        epFile.add(new EPFileLine("4", "BLUE", "2000"));
        epFile.save(inputAllDirPath + "ep_all.txt");

        // 2. 중복 제거 작업
        File appendFile = new File(appendAllPath);

        if (appendFile.exists()) {
            FileUtils.forceDelete(appendFile);
        }

        Files.write(appendFile.toPath(), EPFile.getEPHeader(isAllEP).concat("\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(dbDirPath, isAllEP, appendAllPath);

        File inputDir = new File(inputAllDirPath);
        for (File curFile : inputDir.listFiles()) {
            xodusDbRepository.append(curFile.getPath());
        }

        xodusDbRepository.save();
        xodusDbRepository.print();
        String dbPath = xodusDbRepository.getDbPath();
        xodusDbRepository.close();

        // 3. 검증작업
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("BLUE.2000", "");
        verifyMap.put("RED.1000", "");
        verifyMap.put("RED.2000", "");
        Assert.assertTrue(verifyDbData(dbPath, verifyMap));
    }

    @Test
    public void test02EpUpdateCaseDelete() throws Exception {
        boolean isAllEP = false;

        // 1. 더미 파일 생성
        EPFile epFile = new EPFile(isAllEP);
        epFile.add(new EPFileLine("1", "RED", "1000", "D"));
        epFile.add(new EPFileLine("4", "RED", "2000"));

        epFile.save(inputUpdateDirPath + "ep_update.txt");

        // 2. 중복 제거 작업
        File appendFile = new File(appendUpdatePath);

        if (appendFile.exists()) {
            FileUtils.forceDelete(appendFile);
        }

        Files.write(appendFile.toPath(), EPFile.getEPHeader(isAllEP).concat("\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(dbDirPath, isAllEP, appendUpdatePath);

        File inputDir = new File(inputUpdateDirPath);
        for (File curFile : inputDir.listFiles()) {
            xodusDbRepository.append(curFile.getPath());
        }

        xodusDbRepository.save();
        xodusDbRepository.print();
        String dbPath = xodusDbRepository.getDbPath();
        xodusDbRepository.close();

        // 3. 검증작업
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("BLUE.2000", "");
        verifyMap.put("RED.2000", "");
        Assert.assertTrue(verifyDbData(dbPath, verifyMap));
    }

    @Test
    public void test03EpUpdateCaseIgnore() throws Exception {
        boolean isAllEP = false;

        // 1. 더미 파일 생성
        EPFile epFile = new EPFile(isAllEP);
        epFile.add(new EPFileLine("3", "BLUE", "2000"));

        epFile.save(inputUpdateDirPath + "ep_update.txt");

        // 2. 중복 제거 작업
        File appendFile = new File(appendUpdatePath);

        if (appendFile.exists()) {
            FileUtils.forceDelete(appendFile);
        }

        Files.write(appendFile.toPath(), EPFile.getEPHeader(isAllEP).concat("\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(dbDirPath, isAllEP, appendUpdatePath);

        File inputDir = new File(inputUpdateDirPath);
        for (File curFile : inputDir.listFiles()) {
            xodusDbRepository.append(curFile.getPath());
        }

        xodusDbRepository.save();
        xodusDbRepository.print();
        String dbPath = xodusDbRepository.getDbPath();
        xodusDbRepository.close();

        // 3. 검증작업
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("BLUE.2000", "");
        verifyMap.put("RED.2000", "");
        Assert.assertTrue(verifyDbData(dbPath, verifyMap));
    }

    @Test
    public void test04EpUpdateCaseInsert() throws Exception {
        boolean isAllEP = false;

        // 1. 더미 파일 생성
        EPFile epFile = new EPFile(isAllEP);
        epFile.add(new EPFileLine("4", "BLACK", "2000"));

        epFile.save(inputUpdateDirPath + "ep_update.txt");

        // 2. 중복 제거 작업
        File appendFile = new File(appendUpdatePath);

        if (appendFile.exists()) {
            FileUtils.forceDelete(appendFile);
        }

        Files.write(appendFile.toPath(), EPFile.getEPHeader(isAllEP).concat("\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(dbDirPath, isAllEP, appendUpdatePath);

        File inputDir = new File(inputUpdateDirPath);
        for (File curFile : inputDir.listFiles()) {
            xodusDbRepository.append(curFile.getPath());
        }

        xodusDbRepository.save();
        xodusDbRepository.print();
        String dbPath = xodusDbRepository.getDbPath();
        xodusDbRepository.close();

        // 3. 검증작업
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("BLUE.2000", "");
        verifyMap.put("RED.2000", "");
        verifyMap.put("BLACK.2000", "");
        Assert.assertTrue(verifyDbData(dbPath, verifyMap));
    }

    @Test
    public void test09App() throws Exception {

    }
}
