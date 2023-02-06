package com.friend.your.vprojecte.entity;

import com.friend.your.vprojecte.dto.AppUserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity(name = "inactive_accounts")
public class InactiveAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    private String login;
    private String email;
    private LocalDate birthdate;

    public InactiveAccount(String name, String password, String login, String email, LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.email = email;
        this.birthdate = birthdate;
    }

    public InactiveAccount(AppUserDto userDto) {
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.login = userDto.getLogin();
        this.email = userDto.getEmail();
        this.birthdate = userDto.getBirthdate();
    }
}
