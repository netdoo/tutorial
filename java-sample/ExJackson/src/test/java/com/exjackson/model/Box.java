package com.exjackson.model;

import com.exjackson.databind.BoxNameDeserializer;
import com.exjackson.databind.BoxNameSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Box {

    @JsonDeserialize(using = BoxNameDeserializer.class)
    @JsonSerialize(using = BoxNameSerializer.class)
    String name;
    String color;

    public Box() {

    }

    public Box(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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
                "name : " + this.name + ", " +
                "color : " + this.color  +
                "}";
    }
}
