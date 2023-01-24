package com.friend.your.vprojecte.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostDto {

    private int id;
    private int userId;
    private String description;
    private String url;
    private LocalDate dateOfPost;
}
