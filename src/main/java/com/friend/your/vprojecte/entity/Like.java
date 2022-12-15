package com.friend.your.vprojecte.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(
            name = "user_id",
            nullable = false
    )
    private int userId;

    public Like(int userId) {
        this.userId = userId;
    }
}
