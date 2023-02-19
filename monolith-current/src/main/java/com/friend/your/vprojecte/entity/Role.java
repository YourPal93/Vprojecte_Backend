package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            nullable = false
    )
    private String name;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_type")
    private int roleType;

    public Role(String name) {
        this.name = name;
    }
}
