package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "name",
            unique = true
    )
    private String name;

    @Lob
    private byte[] data;

    public Video(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }
}
