package com.exfile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DirApp {
    final static Logger logger = LoggerFactory.getLogger(DirApp.class);
    final static String HOME_DIR = "C:\\Temp\\EPDB\\";

    static String getDateText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    static void foo(String yyyyMMdd) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = simpleDateFormat.parse(yyyyMMdd);
    }

    static long getExpireTimestamp(int deadLineDate) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(cal.DATE, deadLineDate);
        return cal.getTime().getTime();
    }

    public static void main( String[] args ) throws Exception {

        String path = HOME_DIR + getDateText();

        FileUtils.forceMkdir(new File(path));
        logger.info("create {}", path);

        Collection<File> dirs = FileUtils.listFilesAndDirs(new File(HOME_DIR), new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

        // 3일 이전에 생성된 폴더 삭제
        long expireTimestamp = getExpireTimestamp(-3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        List<File> deleteTargetDirs = dirs.stream().filter(dir -> {
            try {
                return (simpleDateFormat.parse(dir.getName()).getTime() < expireTimestamp);
            } catch(Exception e) {
                logger.info("skip {}", dir.getAbsolutePath());
                return  false;
            }
        }).collect(Collectors.toList());

        deleteTargetDirs.forEach(dir -> {
            logger.info("delete target dir absolutePath {} name {}", dir.getAbsolutePath(), dir.getName());
            try {
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                logger.error("fail to delete dir {}", dir.getAbsolutePath());
            }
        });
    }
}
