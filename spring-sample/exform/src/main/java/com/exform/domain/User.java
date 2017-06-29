package com.exform.domain;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {
    @NotNull @Size(min=4, max=30)  // 유효성 체크 Annotation
    private String email;

    @NotNull @Size(min=4, max=12)
    private String password;

    public User(){
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return password;
    }
}
