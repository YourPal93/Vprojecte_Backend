package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "comments")
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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;


    public Comment(String message, int userId, LocalDateTime creationDate) {

        this.message = message;
        this.userId = userId;
        this.creationDate = creationDate;
    }
}
