package com.exroundrobinhashfile;

public abstract class HashDbValue {
    public abstract String serialize();
    public abstract HashDbValue deserialize(String serialize);
}
