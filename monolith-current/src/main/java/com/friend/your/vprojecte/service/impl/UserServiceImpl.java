package com.friend.your.vprojecte.service.impl;


import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
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
    private final UserPlateRepository userPlateRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

    @Override
    public Page<AppUserPlate> findAllUsers(int pageNo, int pageSize) {
        log.info("Requesting user plates page {} with size {} from the database", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userPlateRepository.findAll(pageable);
    }

    @Override
    public AppUserPlate findUserPlate(String login) {
        log.info("Requesting user plate of user with login {}", login);

        AppUserPlate userPlate = userPlateRepository.findByUserLogin(login)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        return userPlate;
    }

    @Override
    public boolean userExists(String userLogin, String userEmail) {
        log.info("Checking if user with login {} and email {} exists in the database", userEmail, userEmail);

        if(userRepository.existsByLogin(userLogin)) return true;

        return userRepository.existsByEmail(userEmail);
    }

    @Override
    public AppUserDto findUser(Integer id) {
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
    public AppUserDto findUser(String login) {
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

        return userPlateRepository.findByUserLoginContaining(login, pageable);
    }

    @Override
    public AppUserDto saveUser(AppUserDto userDto) {
        log.info("Saving user '{}' to the database ", userDto.getLogin());

        AppUser user = new AppUser(
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getLogin(),
                userDto.getEmail(),
                userDto.getBirthdate());

        user.setRoles(new HashSet<>());
        user.getRoles().add(new Role("ROLE_USER"));

        AppUser savedUser = userRepository.save(user);

        userPlateRepository.save(new AppUserPlate(savedUser.getId(), savedUser.getLogin()));
        userDto.setId(savedUser.getId());
        userDto.setPassword("");
        return userDto;
    }

    @Override
    public AppUserDto updateUser(String userLogin, AppUserDto userDto) {
        log.info("Updating user with login {}", userLogin);

        AppUser user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            userDto.setId(user.getId());
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
    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);

        userRepository.deleteById(id);
        userPlateRepository.deleteById(id);
    }

    @Override
    public Page<PostDto> getUserWall(int pageNo, int pageSize, Integer userId) {
        log.info("requesting user wall of user with id {} page {} page size {}", userId, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userRepository.getUserPosts(userId, pageable);
    }

    @Override
    public PostDto addPostToUser(Post post, Integer userId) {
        log.info("Adding post from user with login {} to user with id {}", post.getUserLogin(),  userId);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime currentDateTime = LocalDateTime.now();

        post.setType(2);
        post.setCreationDate(currentDateTime);
        user.getPosts().add(post);

        postRepository.save(post);
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
