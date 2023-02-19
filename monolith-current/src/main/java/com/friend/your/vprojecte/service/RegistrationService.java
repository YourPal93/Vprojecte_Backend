package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.AppUserCredentialsDto;
import com.friend.your.vprojecte.dto.AppUserDto;

public interface RegistrationService {

    boolean tokenTaken(String userLogin, String userEmail);
    void register(AppUserDto userDto);

    void activateAccount(String tokenCode);
}
