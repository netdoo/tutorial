package com.exmybatis.domain;

import com.exmybatis.domain.serialize.UserDeserializer;
import com.exmybatis.domain.serialize.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    int no;

    String email;
    String name;
    String type;
    String id;
    String password;
    @JsonSerialize(using = UserSerializer.class)
    String someText;

    public void setSomeText(String someText) {
        this.someText = someText;
    }
    public String getSomeText() {
        return this.someText;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return this.id + "," + this.name + "," + this.email;
    }
}

