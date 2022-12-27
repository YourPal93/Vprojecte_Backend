package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import org.springframework.data.domain.Page;

public interface UserService {

    // измени меня
    Page<AppUser> findAll(int pageNo, int pageSize);

    AppUser findById(int id);

    AppUser findByLogin(String login);

    Page<AppUserPlate> findByLoginMatch(int pageNo, int pageSize, String login);
    Boolean exist(String login);

    AppUser save(AppUser user);

    void delete(int id);

    Page<Chat> getChatLogs(int pageNo, int pageSize, AppUser user);
}
