package com.esquery;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class Sample {
    String title;
    String author;
    String category;
    String studio;
    String movie_type;

    @Override
    public String toString() {
        return String.join(",", title, author, category, studio, movie_type);
    }
}
