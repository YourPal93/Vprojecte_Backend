package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(
            name = "message",
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(name = "post_id")
    private int postId;

    @Column(
            name = "user_id",
            nullable = false
    )
    private int userId;

    public Comment(String message, int userId) {

        this.message = message;
        this.userId = userId;
    }
}
