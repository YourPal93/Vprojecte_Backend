package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(
            name = "user_id",
            nullable = false
    )
    private int userId;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "post_id")
    private Set<Like> likes;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    @Column(name = "date_of_post")
    private LocalDate dateOfPost;

    @Column(name = "post_type")
    private int type;

    public Post(int userId, String description, String url, LocalDate dateOfPost) {
        this.userId = userId;
        this.description = description;
        this.url = url;
        this.dateOfPost = dateOfPost;
    }
}
