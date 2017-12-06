package com.exroundrobinhashfile;

import com.google.code.externalsorting.ExternalSort;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HashDb {
    String roundRobinDir;
    int maxRR;
    boolean isOpen;
    boolean deleteOnExit;
    List<HashDbFileWriter> hashDbFileWriters = new ArrayList<>();

    final static Logger logger = LoggerFactory.getLogger(HashDb.class);

    public void open(String roundRobinDir, int maxRR, boolean truncate, boolean deleteOnExit) throws Exception {
        this.maxRR = maxRR;
        this.roundRobinDir = roundRobinDir;
        this.deleteOnExit = deleteOnExit;

        File dir = new File(this.roundRobinDir);

        if (dir.exists()) {
            if (truncate) {
                Arrays.stream(dir.listFiles()).forEach(File::delete);
            }
        } else {
            dir.mkdir();
        }

        for (int i = 0; i < maxRR; i++) {
            hashDbFileWriters.add(new HashDbFileWriter(Paths.get(this.roundRobinDir, String.valueOf(i) + ".dat")));
        }

        this.isOpen = true;
    }

    public void println(String key, String value) {
        int idx = Math.abs(key.hashCode()) % maxRR;
        HashDbFileWriter writer = hashDbFileWriters.get(idx);
        writer.println(value);
    }

    public void close() throws Exception {
        this.hashDbFileWriters.forEach(writer -> {
            writer.close();
        });

        ExecutorService p = Executors.newFixedThreadPool(10);

        this.hashDbFileWriters.forEach(writer -> {
            p.execute(new HashDbDistinctJob(writer.getIoPath()));
        });

        p.shutdown();
        p.awaitTermination(10, TimeUnit.MINUTES);

        this.hashDbFileWriters.clear();
        this.isOpen = false;

        if (deleteOnExit) {
            File dir = new File(this.roundRobinDir);

            if (dir.exists()) {
                Arrays.stream(dir.listFiles()).forEach(File::delete);
            } else {
                dir.mkdir();
            }
        }
    }

    public void sort(Comparator<String> comparator) throws Exception {
        for (int i = 0; i <= maxRR; i++) {
            Path curr = Paths.get(this.roundRobinDir, String.valueOf(i) + ".dat");
            Path sortPath = Paths.get(this.roundRobinDir, String.valueOf(i) + ".sort.dat");
            ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(curr.toFile(), comparator), sortPath.toFile());
        }
    }
}
