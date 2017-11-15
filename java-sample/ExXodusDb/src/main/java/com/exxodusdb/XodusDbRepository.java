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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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

    public XodusDbRepository() {
    }

    public void open(String homeDir) {
        if (homeDir.charAt(homeDir.length()-1) != File.separatorChar) {
            this.homeDir = homeDir + File.separatorChar;
        } else {
            this.homeDir = homeDir;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        this.dbPath = this.homeDir + simpleDateFormat.format(new Date());
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
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getHomeDir() {
        return this.homeDir;
    }

    public String getDbPath() {
        return this.dbPath;
    }

    long getExpireTimestamp(int deadLineDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(cal.DATE, deadLineDate);
        return cal.getTime().getTime();
    }

    private boolean isUpsertDeal(Transaction txn, Store store, EPTSVData curr, boolean isAllEP) throws Exception {

        ByteIterable existData = store.get(txn, stringToEntry(curr.getNamedKey()));

        if (null == existData) {
            // 중복된 딜이 없는 경우.
            return true;
        }

        // 중복된 딜이 발견된 경우
        EPTSVData exist = new EPTSVData(entryToString(existData), isAllEP);

        if (isAllEP) {
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

    public void append(Path readPath, Path appendPath, boolean isAllEP) {
        List<String> targetEPList = new ArrayList<>();


        LOGGER.debug("Append Start {}", appendPath.toAbsolutePath().toString());

        /// append 할 파일이 없으면 생성함.
        try {
            FileUtils.touch(appendPath.toFile());
        } catch (IOException e) {
            LOGGER.error("append path {}", appendPath, e);
        }

        this.env.executeInTransaction(txn -> {
            String line;
            int lineCount = 0;
            try (BufferedReader in = Files.newBufferedReader(readPath, StandardCharsets.UTF_8);
                 BufferedWriter out = Files.newBufferedWriter(appendPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND);) {
                while ((line=in.readLine()) != null) {

                    if (StringUtils.startsWithIgnoreCase(line, "id\t")) {
                        // 헤더 라인은 무시함.
                        continue;
                    }

                    try {
                        EPTSVData curr = new EPTSVData(line, isAllEP);

                        if (StringUtils.equalsIgnoreCase(curr.getOpClass(), "D")) {
                            // class가 D인 경우는 무조건 내보냄.
                            out.write(line + "\n");
                            LOGGER.debug("[내보냄] {}", line);
                            store.delete(txn, stringToEntry(curr.getNamedKey()));
                            continue;
                        }

                        if (isUpsertDeal(txn, store, curr, isAllEP)) {
                            out.write(line + "\n");
                            LOGGER.debug("[내보냄] {}", line);
                            this.store.put(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                        }

                        if (++lineCount % 10_000 == 0) {
                            LOGGER.info("process {} tsv lines", lineCount);
                        }

                    } catch(Exception e) {
                        LOGGER.error("", e);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        });
    }

    public void append(String tsvReadPath, String tsvAppendPath, Environment env, Store store, boolean isAllEP) {
        append(Paths.get(tsvReadPath), Paths.get(tsvAppendPath), env, store, isAllEP);
    }


    public void removeDirs() {

        if (this.homeDir == null)
            return;

        Collection<File> dirs = FileUtils.listFilesAndDirs(new File(this.homeDir), new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

        // 3일 이전에 생성된 폴더 삭제
        long expireTimestamp = getExpireTimestamp(-3);
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

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
}
