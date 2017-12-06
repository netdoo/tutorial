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

public class HashDbWriter implements AutoCloseable {
    String roundRobinDir;
    int maxRR;
    boolean isOpen;
    boolean deleteOnExit;
    HashDbEvent hashDbEvent;
    List<HashDbFileWriter> hashDbFileWriters = new ArrayList<>();

    final static Logger logger = LoggerFactory.getLogger(HashDbWriter.class);

    public HashDbWriter(String roundRobinDir, int maxRR, HashDbEvent hashDbEvent, boolean truncate, boolean deleteOnExit) throws Exception {
        this.maxRR = maxRR;
        this.roundRobinDir = roundRobinDir;
        this.deleteOnExit = deleteOnExit;
        this.hashDbEvent = hashDbEvent;

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

    public void put(String key, HashDbValue value) {
        int idx = Math.abs(key.hashCode()) % maxRR;
        HashDbFileWriter writer = hashDbFileWriters.get(idx);
        writer.setChanged(true);
        writer.println(HashDb.serializeKeyValue(key, value.serialize()));
    }

    @Override
    public void close() throws Exception {
        this.hashDbFileWriters.forEach(writer -> {
            writer.close();
        });

        ExecutorService p = Executors.newFixedThreadPool(10);

        this.hashDbFileWriters
                .stream()
                .filter(writer -> writer.isChanged())
                .forEach(writer -> {
                    p.execute(new HashDbIndexJob(writer.getIoPath(), this.hashDbEvent));
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
}
