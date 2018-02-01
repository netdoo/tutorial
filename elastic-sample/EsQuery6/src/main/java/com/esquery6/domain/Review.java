package com.esquery6.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Review {

    int star;
    String message;
    boolean like;

    public void setStar(int star) {
        this.star = star;
    }

    public int getStar() {
        return this.star;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean getLike() {
        return this.like;
    }
}
