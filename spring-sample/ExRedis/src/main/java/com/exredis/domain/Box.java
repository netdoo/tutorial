package com.exredis.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Box implements Serializable {

    String id;
    String name;
    List<String> colors;

    public Box() {

    }

    public Box(String id, String name, List<String> colors) {
        this.id = id;
        this.name = name;
        this.colors = colors;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getColors() {
        return colors;
    }

    @Override
    public String toString() {
        return "id = " + this.id + ", "
                + "name = " + this.name + ", "
                + "colors = " + this.colors;
    }
}
