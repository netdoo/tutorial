package com.esquery6.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Market {
    String name;
    long price;

    @JsonIgnore
    String docId;

    public Market(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Market(String docId, String name, long price) {
        this.docId = docId;
        this.name = name;
        this.price = price;
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

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice() {
        return this.price;
    }
}
