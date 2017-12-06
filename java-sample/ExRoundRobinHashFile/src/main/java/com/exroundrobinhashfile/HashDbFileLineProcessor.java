package com.exroundrobinhashfile;

import org.apache.commons.lang3.tuple.Pair;

@FunctionalInterface
public interface HashDbFileLineProcessor {
    public Pair<String, String> onProcess(String line);
}
