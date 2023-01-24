package com.friend.your.vprojecte.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Integer id;
    private String message;
    private String userLogin;
    private LocalDateTime createdDate;
}
