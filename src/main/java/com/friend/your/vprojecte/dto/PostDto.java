package com.friend.your.vprojecte.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private Integer id;
    private String userLogin;
    private String description;
    private String url;
    private LocalDateTime creationDate;
}
