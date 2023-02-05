package com.friend.your.vprojecte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_plates")
public class AppUserPlate {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_login")
    private String userLogin;

}
