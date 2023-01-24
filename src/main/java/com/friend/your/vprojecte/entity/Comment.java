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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUserPlate createdBy;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;


    public Comment(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
