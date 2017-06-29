package com.exmybatis.domain;

import org.springframework.beans.factory.annotation.Qualifier;

public class User {
    String user_email;
    String user_name;
    String user_type;
    String user_id;

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserId() {
        return this.user_id;
    }

    public String getEmailAddress() {
        return this.user_email;
    }

    public void setEmailAddress(String user_email) {
        this.user_email = user_email;
    }

    public String getUserName() {
        return this.user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public void setUserType(String user_type) {
        this.user_type = user_type;
    }

    public String getUserType() {
        return this.user_type;
    }

    @Override
    public String toString() {
        return this.user_id + "," + this.user_name + "," + this.user_email;
    }
}


