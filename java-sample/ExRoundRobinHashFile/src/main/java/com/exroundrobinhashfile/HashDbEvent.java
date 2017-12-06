package com.exroundrobinhashfile;

public interface HashDbEvent {
    public void onIndex(String key, String serializeValue);
    public boolean onOverWrite(String oldSerializeValue, String newSerializeValue);
}
