package com.exxodusdb;

import com.exxodusdb.domain.EPTSVData;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

/**
 * Created by jhkwon78 on 2017-11-15.
 */
public class XodusDbRepository {
    String homeDir = "C:\\Temp\\Xodus.DB";
    String dbPath;
    Environment env = null;
    Store store = null;
    boolean isAllEP = false;
    String appendPath;
    Map<String, String> virtualFileMap = new HashMap<>();

    public XodusDbRepository() {
    }

    public void open(String homeDir, boolean isAllEP, String appendPath) throws Exception {
        if (homeDir.charAt(homeDir.length()-1) != File.separatorChar) {
            this.homeDir = homeDir + File.separatorChar;
        } else {
            this.homeDir = homeDir;
        }

        this.isAllEP = isAllEP;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        this.dbPath = this.homeDir + simpleDateFormat.format(new Date());

        if (this.isAllEP) {
            // 전체 EP 재 생성시에, 기존에 누적된 EP 데이터를 삭제함.
            FileUtils.deleteDirectory(new File(this.dbPath));
            LOGGER.info("Delete Dir {}", this.dbPath);
        } else {
            this.virtualFileMap.clear();
        }

        this.appendPath = appendPath;
        this.env = Environments.newInstance(this.dbPath);
        this.store = env.computeInTransaction(txn ->
                env.openStore("EP", StoreConfig.WITHOUT_DUPLICATES, txn));
    }

    public boolean isOpen() {

        if (this.env != null)
            return true;

        return false;
    }

    public void close() {

        if (env != null) {
            env.close();
        }

        env = null;
        this.virtualFileMap.clear();
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getHomeDir() {
        return this.homeDir;
    }

    public void setAppendPath(String appendPath) {
        this.appendPath = appendPath;
    }

    public String getAppendPath() {
        return this.appendPath;
    }

    public String getDbPath() {
        return this.dbPath;
    }

    static int findCount = 0;

    private boolean isUpsertDeal(Transaction txn, EPTSVData curr) throws Exception {

        ByteIterable existData = this.store.get(txn, stringToEntry(curr.getNamedKey()));

        if (null == existData) {
            // 중복된 딜이 없는 경우.
            return true;
        }

        // 중복된 딜이 발견된 경우
        EPTSVData exist = new EPTSVData(entryToString(existData), this.isAllEP);

        if (this.isAllEP) {
            // 전체 EP인 경우는 딜 아이디가 적은딜을 내보냄.
            if (Long.valueOf(curr.getId()) < Long.valueOf(exist.getId())) {
                return true;
            }
        } else {
            // 딜 아이디가 적거나 같은딜을 내보냄 - 프리스타일 협의함.
            if (Long.valueOf(curr.getId()) <= Long.valueOf(exist.getId())) {
                return true;
            }
        }



        /*
        if (curr.getTime() == exist.getTime()) {
            // 시간이 같은 경우 딜 아이디가 적은딜을 내보냄.
            if (Long.valueOf(curr.getId()) < Long.valueOf(exist.getId())) {
                return true;
            }
        } else {
            // 시간이 다른 경우 판매시작일이 더 빠른딜을 내보냄.
            if (curr.getTime() < exist.getTime()) {
                return true;
            }
        }
        */

        // 그 외 경우는 무시함.
        return false;
    }

    public void append(Path readPath) throws Exception {

        if (!isOpen()) {
            LOGGER.error("not opened");
            return;
        }

        LOGGER.debug("@@@ Start {} isAllEP {}", appendPath, this.isAllEP);

        this.env.executeInTransaction(txn -> {
            String line;
            long lineCount = 0;
            try (BufferedReader in = Files.newBufferedReader(readPath, StandardCharsets.UTF_8)) {
                while ((line=in.readLine()) != null) {
                    lineCount++;

                    if (StringUtils.startsWithIgnoreCase(line, "id\t")) {
                        // 헤더 라인은 무시함.
                        continue;
                    }

                    try {
                        EPTSVData curr = new EPTSVData(line, isAllEP);

                        if (StringUtils.equalsIgnoreCase(curr.getOpClass(), "D")) {
                            // class가 D인 경우는 중복체크를 하지 않고, 무조건 내보냄.
                            //LOGGER.debug("[내보냄] {}", line);
                            if (!this.isAllEP)
                                this.virtualFileMap.put(curr.getNamedKey(), line);

                            store.delete(txn, stringToEntry(curr.getNamedKey()));
                            continue;
                        }

                        if (isUpsertDeal(txn, curr)) {
                            //LOGGER.debug("[내보냄] {}", line);
                            if (!this.isAllEP)
                                this.virtualFileMap.put(curr.getNamedKey(), line);

                            this.store.put(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                        } else {
                            LOGGER.info("[중복딜] {}", line);
                        }

                    } catch(Exception e) {
                        LOGGER.error("Error to process line {}", line, e);
                    }
                }

                LOGGER.info("process path {} lines {}", readPath, lineCount);
            } catch (Exception e) {
                LOGGER.error("IO Error ",  e);
            }
        });

        LOGGER.debug("@@@ Finish {} isAllEP {}", appendPath, this.isAllEP);
    }

    public void append(String tsvReadPath) throws Exception {
        append(Paths.get(tsvReadPath));
    }

    public void save() {
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(this.appendPath), StandardCharsets.UTF_8, StandardOpenOption.APPEND))) {
            if (this.isAllEP) {
                this.env.executeInReadonlyTransaction(txn -> {
                    try (Cursor cursor = store.openCursor(txn)) {
                        while (cursor.getNext()) {
                            out.println(entryToString(cursor.getValue()));
                        }
                    }
                });
            } else {
                this.virtualFileMap.forEach((key, value) -> {
                    out.println(value);
                });
            }
        } catch (Exception e) {
            LOGGER.error("IO Error ", e);
        }
    }

    public static void removeDirs(String homeDir, int daysAfter) {

        Collection<File> dirs = FileUtils.listFilesAndDirs(new File(homeDir), new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

        // 3일 이전에 생성된 폴더 삭제
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(cal.DATE, daysAfter);
        long expireTimestamp = cal.getTime().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        List<File> deleteTargetDirs = dirs.stream().filter(dir -> {
            try {
                return (simpleDateFormat.parse(dir.getName()).getTime() < expireTimestamp);
            } catch(Exception e) {
                LOGGER.info("skip {}", dir.getAbsolutePath());
                return  false;
            }
        }).collect(Collectors.toList());

        deleteTargetDirs.forEach(dir -> {
            LOGGER.info("delete target dir absolutePath {} name {}", dir.getAbsolutePath(), dir.getName());
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                LOGGER.error("fail to delete dir {}", dir.getAbsolutePath());
            }
        });
    }

    public void print() {
        this.env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    LOGGER.info("{} tt=> {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(XodusDbRepository.class);
}
