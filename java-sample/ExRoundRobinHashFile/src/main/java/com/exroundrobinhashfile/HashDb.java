package com.exroundrobinhashfile;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class HashDb {
    public static String serializeKeyValue(String key, String value) {
        return key + "\t" + value;
    }

    public static Pair<String, String> deserializeKeyValue(String serialize) {
        int pos = serialize.indexOf("\t");
        return new ImmutablePair<>(serialize.substring(0, pos), serialize.substring(pos+1));
    }
}
