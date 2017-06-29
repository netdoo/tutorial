package com.exresttemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    int postId;
    int id;
    String name;
    String email;
    String body;

    @Override
    public String toString() {
        return "name : " + name + ", email : " + email + ", body : " + body;
    }
}
