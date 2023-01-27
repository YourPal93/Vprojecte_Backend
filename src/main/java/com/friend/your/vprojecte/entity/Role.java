package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(
            name = "name",
            nullable = false,
            unique = true
    )
    private String name;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Integer userId;

    @Column(name = "role_type")
    private int type;

    public Role(String name, Integer userId) {
        this.name = name;
        this.userId = userId;
    }

    public Role(String name, Integer userId, int type) {
        this.name = name;
        this.userId = userId;
        this.type = type;
    }
}
