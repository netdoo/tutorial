package com.esjest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Color extends AbstractDocument {

    public Color() {
    }

    public Color(String docId, String name, String createDate) {
        this.docId = docId;
        this.name = name;
        this.createDate = createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    @Override
    public String toString() {
        return "Color {" +
                "name : " + this.name + ", " +
                "createDate : " + this.createDate +
                 "}";
    }

    String name;
    String createDate;
}
