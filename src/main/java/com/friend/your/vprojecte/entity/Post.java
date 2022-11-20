package com.friend.your.vprojecte.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Post {

    private int id;
    private String description;
    private String url;
    private LocalDate date;

    public Post() {
    }

    public Post(String description, String url, LocalDate date) {
        this.description = description;
        this.url = url;
        this.date = date;
    }
}
