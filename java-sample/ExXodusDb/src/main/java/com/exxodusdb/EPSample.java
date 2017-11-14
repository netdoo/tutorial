package com.exxodusdb;

import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class EPSample {
    final static Logger logger = LoggerFactory.getLogger(EPSample.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    static void print(Environment env, Store store, String title) {
        logger.info("======================== {} =====================", title);
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    logger.info("{} \t\t=> {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });
        logger.info("===============================================================");
    }

    static void printEP(List<String> epList, String title) {
        logger.info("======================== {} =====================", title);
        epList.forEach(ep -> {
            logger.info("[내보냄] {}", ep);
        });
        logger.info("===============================================================");
    }

    static class BaseEPRow {
        String id;
        Date date;

        public String getId() {
            return this.id;
        }

        public Date getDate() {
            return this.date;
        }

        public long getTime() {
            return this.date.getTime();
        }

        public Date parseDate(String dateText) throws Exception {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
            return simpleDateFormat.parse(dateText);
        }
    }

    static class EPTSVRow extends BaseEPRow {

        String namedKey;
        String namedValue;

        public EPTSVRow(String line) throws Exception {
            String cols[] = line.trim().split("\t");
            this.id = cols[0];
            this.namedKey = cols[1] + "." + cols[2];
            this.namedValue = cols[0] + "\t" + cols[3];     // 딜아이디\t판매시작일
            this.date = parseDate(cols[3]);
        }

        public String getNamedKey() {
            return this.namedKey;
        }

        public String getNamedValue() {
            return this.namedValue;
        }
    }

    static class EPDbRow extends BaseEPRow {

        public EPDbRow(ByteIterable data) throws Exception {
            String line = entryToString(data);
            String cols[] = line.split("\t");
            this.id = cols[0];
            this.date = parseDate(cols[1]);
        }
    }

    static List<String> makeAllEp(String tsvPath, Environment env, Store store) {
        List<String> targetEPList = new ArrayList<>();

        env.executeInTransaction(txn -> {
            String line;
            try (BufferedReader in = Files.newBufferedReader(Paths.get(tsvPath), StandardCharsets.UTF_8)) {
                while ((line=in.readLine()) != null) {
                    try {
                        EPTSVRow curr = new EPTSVRow(line);

                        ByteIterable existData = store.get(txn, stringToEntry(curr.getNamedKey()));
                        if (null == existData) {
                            // 중복된 딜이 없는 경우.
                            targetEPList.add(line);
                            store.add(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                        } else {
                            // 중복된 딜이 발견된 경우
                            EPDbRow exist = new EPDbRow(existData);

                            if (curr.getTime() == exist.getTime()) {
                                // 시간이 같은 경우 딜 아이디가 적은딜을 내보냄.
                                if (Long.valueOf(exist.getId()) < Long.valueOf(curr.getId())) {
                                    // 기존 딜 아이디가 현재 딜 아이디 보다 작은 경우 무시함.
                                } else {
                                    // 기존 딜 아이디 보다 현재 딜 아이디가 작은 경우, 내보냄.
                                    targetEPList.add(line);
                                    store.put(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                                }
                            } else {
                                // 시간이 다른 경우 판매시작일이 더 빠른딜을 내보냄.
                                if (exist.getTime() < curr.getTime()) {
                                    // 기존 딜 아이디의 판매시작일이 현재 딜 아이디의 판매시작일 보다 빠른 경우, 무시함.
                                } else {
                                    // 기존 딜 아이디의 판매시작일 보다, 현재 딜 아이디의 판매시작일이 빠른 경우, 내보냄.
                                    targetEPList.add(line);
                                    store.put(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                                }
                            }
                        }
                    } catch(Exception e) {
                        logger.error("", e);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        });

        return targetEPList;
    }

    public static void main( String[] args ) throws Exception {
        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("EP", StoreConfig.WITHOUT_DUPLICATES, txn));

        List<String> allEP = makeAllEp("C:\\temp\\tsv_ep_all.txt", env, store);
        printEP(allEP, "전체EP");
        print(env, store, "EPDB 상태");

        List<String> partEP = makeAllEp("C:\\temp\\tsv_ep_part_201711141020.txt", env, store);
        printEP(partEP, "요약EP");
        print(env, store, "EPDB 상태");

        partEP = makeAllEp("C:\\temp\\tsv_ep_part_201711141021.txt", env, store);
        printEP(partEP, "요약EP");
        print(env, store, "EPDB 상태");

        env.close();
    }
}
