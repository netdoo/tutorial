package com.esquery6.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class BookStoreDoc {
    Long isbn;
    List<Author> author;
    @JsonIgnore
    String docId;

    public BookStoreDoc() {
    }

    public BookStoreDoc(String docId, Long isbn, List<Author> author) {
        this.docId = docId;
        this.isbn = isbn;
        this.author = author;
    }


    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return this.docId;
    }


    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public Long getIsbn() {
        return this.isbn;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public List<Author> getAuthor() {
        return this.author;
    }

    @Override
    public String toString() {
        return "BookStoreDoc {" +
                "isbn : " + isbn + "," +
                "author : " + author +
                "}";
    }
}
