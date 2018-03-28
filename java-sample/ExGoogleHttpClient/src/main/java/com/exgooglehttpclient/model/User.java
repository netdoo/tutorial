package com.exgooglehttpclient.model;

import com.google.api.client.util.Key;

public class User {
    @Key
    private String login;
    @Key
    private long id;
    @Key("email")
    private String email;

    @Override
    public String toString() {
        return "User {" +
                "id : " + id + ", " +
                "login : " + login + ", " +
                "email : " + email +
                "}\n";
    }
}
