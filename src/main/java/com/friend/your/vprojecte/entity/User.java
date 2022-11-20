package com.friend.your.vprojecte.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    private String name;
    private String login;
    private LocalDate birthdate;

    public User() {
    }

    public User(String name, String login, LocalDate birthdate) {
        this.name = name;
        this.login = login;
        this.birthdate = birthdate;
    }
}
