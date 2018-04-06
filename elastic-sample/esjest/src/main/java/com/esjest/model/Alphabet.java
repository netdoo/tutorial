package com.esjest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Alphabet {
    String name;
    int code;

    @JsonIgnore
    String docId;

    public Alphabet() {

    }

    public Alphabet(String docId, char c) {
        this.docId = docId;
        this.name = String.valueOf(c);
        this.code = (int)c;
    }

    public Alphabet(String docId, String name, int code) {
        this.docId = docId;
        this.name = name;
        this.code = code;
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

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
