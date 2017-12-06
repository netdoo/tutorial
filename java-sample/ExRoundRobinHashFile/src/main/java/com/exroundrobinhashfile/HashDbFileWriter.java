package com.exroundrobinhashfile;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HashDbFileWriter extends PrintWriter {

    Path ioPath;

    public HashDbFileWriter(Path ioPath) throws Exception {
        super(new PrintWriter(Files.newBufferedWriter(ioPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)));
        this.ioPath = ioPath;
    }

    public Path getIoPath() {
        return this.ioPath;
    }
}
