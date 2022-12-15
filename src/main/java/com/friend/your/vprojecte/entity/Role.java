package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
