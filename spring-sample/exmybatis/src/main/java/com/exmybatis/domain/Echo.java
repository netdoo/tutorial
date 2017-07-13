package com.exmybatis.domain;


public class Echo {
    String text;
    String name;

    public Echo() {
    }

    public Echo(String text) {
        this.text = text;
    }

    public Echo(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
