package com.friend.your.vprojecte.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "post_likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(
            name = "user_id",
            nullable = false
    )
    private Integer userId;

    @Column(name = "post_id")
    private Integer postId;

    public Like(Integer userId) {
        this.userId = userId;
    }
}
