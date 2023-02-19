package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "user_login",
            nullable = false
    )
    private String userLogin;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "post_type")
    private int type;

    public Post(String userLogin, String description, String url, LocalDateTime creationDate) {
        this.userLogin = userLogin;
        this.description = description;
        this.url = url;
        this.creationDate = creationDate;
    }
}
