package com.exstream.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jhkwon78 on 2018-02-20.
 */
public class Box {

    String color;

    final Logger logger = LoggerFactory.getLogger(Box.class);

    public Box() {
        logger.info("Box 생성자 호출됨");
    }

    public Box(String color) {
        this.color = color;
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
                "color : '" + this.color + "'" +
                "}";
    }
}
