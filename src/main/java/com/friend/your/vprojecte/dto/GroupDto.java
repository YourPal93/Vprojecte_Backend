package com.friend.your.vprojecte.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    private Integer id;
    private String name;
    private Boolean closed;

}
