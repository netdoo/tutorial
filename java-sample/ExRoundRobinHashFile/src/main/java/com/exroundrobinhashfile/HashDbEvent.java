package com.exroundrobinhashfile;

@FunctionalInterface
public interface HashDbEvent {
    public void onIndex(String key, String serializeValue);
}
