package com.esquery6.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Market {
    String name;
    long price;
    String country;
    String location;
    List<Product> products;

    @JsonIgnore
    String docId;


    public Market(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Market(String docId, String name, long price, String country) {
        this.docId = docId;
        this.name = name;
        this.price = price;
        this.country = country;
    }

    public Market(String docId, String name, long price, String country, String location, List<Product> products) {
        this.docId = docId;
        this.name = name;
        this.price = price;
        this.country = country;
        this.location = location;
        this.products = products;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return this.products;
    }
}
