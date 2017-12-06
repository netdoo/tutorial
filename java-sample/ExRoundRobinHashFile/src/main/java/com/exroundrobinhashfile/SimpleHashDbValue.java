package com.exroundrobinhashfile;

public class SimpleHashDbValue extends HashDbValue {
    String line;
    long updateTimeAt;

    public SimpleHashDbValue() {
    }

    public SimpleHashDbValue(String serialize) {
        int pos = serialize.indexOf("\t");
        this.line = serialize.substring(pos+1);
        this.updateTimeAt = Long.valueOf(serialize.substring(0, pos));
    }

    public SimpleHashDbValue(String line, long updateTimeAt) {
        this.line = line;
        this.updateTimeAt = updateTimeAt;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public void setUpdateTimeAt(long updateTimeAt) {
        this.updateTimeAt = updateTimeAt;
    }

    public long getUpdateTimeAt() {
        return this.updateTimeAt;
    }

    @Override
    public String serialize() {
        return this.updateTimeAt + "\t" + this.line;
    }

    @Override
    public HashDbValue deserialize(String serialize) {
        int pos = serialize.indexOf("\t");
        return new SimpleHashDbValue(serialize.substring(pos+1), Long.valueOf(serialize.substring(0, pos)));
    }
}
