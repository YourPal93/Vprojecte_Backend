package com.friend.your.vprojecte.service.impl;


import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.AppUserCredentialsDto;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.service.UserService;
import com.friend.your.vprojecte.utility.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPlateJPARepository userPlateRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<AppUserPlate> findAll(int pageNo, int pageSize) {
        log.info("Requesting user plates page {} with size {} from the database", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userPlateRepository.findAll(pageable);
    }

    @Override
    public AppUserPlate findUserPlate(String login) {
        log.info("Requesting user plate of user with login {}", login);

        AppUserPlate userPlate = userPlateRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        return userPlate;
    }

    @Override
    public AppUserDto findById(Integer id) {
        log.info("Requesting user with id: {}", id);

        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        AppUserDto userDto = new AppUserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        userDto.setBirthdate(user.getBirthdate());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    @Override
    public AppUserDto findByLogin(String login) {
        log.info("Requesting user with login: {}", login);

        AppUser user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        AppUserDto userDto = new AppUserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setBirthdate(user.getBirthdate());

        return userDto;
    }

    @Override
    public Page<AppUserPlate> findByLoginMatch(int pageNo, int pageSize, String login) {
        log.info("Request user plate matches with login {} page no {} page size {}", login, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userPlateRepository.findByLoginContaining(login, pageable);
    }

    @Override
    public Boolean exist(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public AppUserDto save(AppUserDto userDto) {
        log.info("Saving user '{}' to the database ", userDto.getLogin());

        AppUser user = new AppUser(
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getLogin(),
                userDto.getEmail(),
                userDto.getBirthdate());

        user.setRoles(new HashSet<>());

        AppUser savedUser = userRepository.save(user);
        AppUserPlate savedUserPlate = new AppUserPlate(savedUser.getId(), savedUser.getLogin());

        roleService.addRoleToUser(user, new Role("ROLE_USER", savedUser.getId()));
        userPlateRepository.save(savedUserPlate);

        userDto.setId(savedUser.getId());

        return userDto;
    }

    @Override
    public AppUserDto update(String userLogin, AppUserDto userDto) {
        log.info("Updating user with login {}", userLogin);

        AppUser user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            userDto.setPassword(null);
            return userDto;
        }

        user.setName(userDto.getName());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());

        userRepository.save(user);

        userDto.setId(user.getId());

        return userDto;
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting user with id: {}", id);

        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
        userPlateRepository.deleteById(user.getId());
    }

    @Override
    public Page<Chat> getChatLogs(int pageNo, int pageSize, String loginOfUser) {
        log.info("Requesting chat logs of user {}", loginOfUser);

        AppUser user = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Chat> pageOfChatLogs = PageUtil.pageFromList(pageNo, pageSize, user.getChatLog());

        return pageOfChatLogs;
    }

    @Override
    public Page<PostDto> getUserWall(int pageNo, int pageSize, Integer userId) {
        log.info("requesting user wall of user with id {} page {} page size {}", userId, pageNo, pageSize);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Post> posts = user.getPosts();
        List<PostDto> userWallDtos = new ArrayList<>();

        int start = pageNo * pageSize;
        int end = start + pageSize;
        if(end > posts.size()) {
            end = posts.size();
        }

        for(Post post : posts.subList(start, end)) {

            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setUserLogin(postDto.getUserLogin());
            postDto.setDescription(post.getDescription());
            postDto.setUrl(post.getUrl());
            postDto.setCreationDate(post.getCreationDate());
            userWallDtos.add(postDto);
        }

        Page<PostDto> userWall = PageUtil.listToPage(pageNo, pageSize, userWallDtos, posts.size());

        return userWall;
    }

    @Override
    public PostDto addPostToUser(Post post, Integer userId) {
        log.info("String adding post to user with id {}", userId);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime currentDateTime = LocalDateTime.now();

        post.setType(2);
        post.setCreationDate(currentDateTime);
        user.getPosts().add(post);

        userRepository.save(user);

        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setUserLogin(post.getUserLogin());
        postDto.setDescription(post.getDescription());
        postDto.setUrl(post.getUrl());
        postDto.setCreationDate(currentDateTime);

        return postDto;
    }

}
