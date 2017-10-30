package com.exthymeleaf.domain;

import java.io.Serializable;

public class Box implements Serializable {
    String code;
    String color;

    public Box() {
    }

    public Box(String code, String color) {
        this.code = code;
        this.color = color;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "Box {" +
                "code : " + code +
                " ,color : " + color +
                "}";
    }
}
