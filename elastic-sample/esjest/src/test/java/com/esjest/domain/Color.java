package com.esjest.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Color {

    String name;
    String createDate;

    @JsonIgnore
    String docId;

    public Color() {

    }

    public Color(String docId, String name, String createDate) {
        this.docId = docId;
        this.name = name;
        this.createDate = createDate;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return this.docId;
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
}
