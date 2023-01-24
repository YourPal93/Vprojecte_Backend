package com.friend.your.vprojecte.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppUserDto {

    private String name;
    private String password;
    private String login;
    private String email;
    private LocalDate birthdate;

}
