package com.exroundrobinhashfile;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class HashDbFileWriter extends PrintWriter {

    Path ioPath;
    boolean changed;

    public HashDbFileWriter(Path ioPath, OpenOption... options) throws Exception {
        super(new PrintWriter(Files.newBufferedWriter(ioPath, StandardCharsets.UTF_8, options)));
        this.ioPath = ioPath;
    }



    public Path getIoPath() {
        return this.ioPath;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return this.changed;
    }
}
