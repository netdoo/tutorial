package com.esjest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDocument {

    public AbstractDocument() {
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return this.docId;
    }

    @JsonIgnore
    String docId;
}
