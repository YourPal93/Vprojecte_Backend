package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            name = "message",
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUserPlate createdBy;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;


    public Comment(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}
