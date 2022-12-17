package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Chat;
import org.springframework.data.domain.Page;

public interface UserService {

    // измени меня
    public Page<AppUser> findAll(int pageNo, int pageSize);

    public AppUser findById(int id);

    public AppUser findByLogin(String login);
    Boolean exist(String login);

    public AppUser save(AppUser user);

    public void delete(int id);

    public Page<Chat> getChatLogs(int pageNo, int pageSize, AppUser user);
}
