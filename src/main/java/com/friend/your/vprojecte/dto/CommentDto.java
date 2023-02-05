package com.friend.your.vprojecte.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    private String message;
    private String userLogin;
    private LocalDateTime creationDate;
}
