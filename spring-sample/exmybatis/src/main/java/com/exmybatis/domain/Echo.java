package com.exmybatis.domain;


public class Echo {
    String text;

    public Echo() {

    }

    public Echo(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
