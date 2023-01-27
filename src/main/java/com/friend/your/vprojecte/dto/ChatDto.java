package com.friend.your.vprojecte.dto;

import com.friend.your.vprojecte.entity.AppUserPlate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

    Integer id;
    AppUserPlate receiverPlate;
}
