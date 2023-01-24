package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;

public interface UserService {

    // измени меня
    Page<AppUser> findAll(int pageNo, int pageSize);

    AppUserPlate findUserPlate(String login);

    AppUser findById(int id);

    AppUserDto findByLogin(String login);

    Page<AppUserPlate> findByLoginMatch(int pageNo, int pageSize, String login);
    Boolean exist(String login);

    AppUser save(AppUserDto userDto);

    AppUser update(AppUserDto userDto);

    void delete(int id);

    Page<Chat> getChatLogs(int pageNo, int pageSize, String loginOfUser);

    Page<PostDto> getUserWall(int pageNo, int pageSize, int userId);

    void addPostToUser(Post post, int userId);
}
