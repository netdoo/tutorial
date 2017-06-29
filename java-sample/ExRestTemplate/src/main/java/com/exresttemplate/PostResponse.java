package com.exresttemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {
    String id;

    @Override
    public String toString() {
        return id;
    }
}
