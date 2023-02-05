package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.AppUserCredentialsDto;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;

public interface UserService {

    // измени меня
    Page<AppUserPlate> findAllUsers(int pageNo, int pageSize);

    AppUserPlate findUserPlate(String login);

    boolean userExists(String userLogin, String userEmail);

    AppUserDto findUser(Integer id);

    AppUserDto findUser(String login);

    Page<AppUserPlate> findByLoginMatch(int pageNo, int pageSize, String login);

    AppUserDto saveUser(AppUserDto userDto);

    AppUserDto updateUser(String userLogin, AppUserDto userDto);

    void deleteUser(Integer id);

    Page<PostDto> getUserWall(int pageNo, int pageSize, Integer userId);

    PostDto addPostToUser(Post post, Integer userId);
}
