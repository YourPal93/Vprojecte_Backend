package com.friend.your.vprojecte.service.impl;


import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.AppUserDto;
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

import java.util.HashSet;

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
    public Page<AppUser> findAll(int pageNo, int pageSize) {
        log.info("Requesting users page {} with size {} from the database", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userRepository.findAll(pageable);
    }

    @Override
    public AppUserPlate findUserPlate(String login) {
        log.info("Requesting user plate of user with login {}", login);

        AppUserPlate userPlate = userPlateRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        return userPlate;
    }

    @Override
    public AppUser findById(int id) {
        log.info("Requesting user with id: {}", id);

        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }

    @Override
    public AppUserDto findByLogin(String login) {
        log.info("Requesting user with login: {}", login);

        AppUser user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        AppUserDto userDto = new AppUserDto();

        userDto.setName(user.getName());
        userDto.setLogin(userDto.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setBirthdate(user.getBirthdate());
        userDto.setPassword("");

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
    public AppUser save(AppUserDto userDto) {
        log.info("Saving user '{}' to the database ", userDto.getLogin());

        AppUser user = new AppUser(
                userDto.getName(),
                userDto.getPassword(),
                userDto.getLogin(),
                userDto.getEmail(),
                userDto.getBirthdate());

        user.setRoles(new HashSet<>());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        AppUser savedUser = userRepository.save(user);
        AppUserPlate savedUserPlate = new AppUserPlate(savedUser.getId(), savedUser.getLogin());

        roleService.addRoleToUser(user, new Role("ROLE_USER", savedUser.getId()));
        userPlateRepository.save(savedUserPlate);

        return savedUser;
    }

    @Override
    public AppUser update(AppUserDto userDto) {
        log.info("Updating user with login {}", userDto.getLogin());

        AppUser user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return userRepository.save(user);
        }

        user.setName(userDto.getName());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());

        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
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
    public Page<Post> getUserWall(int pageNo, int pageSize, int userId) {
        log.info("requesting user wall of user with id {} page {} page size {}", userId, pageNo, pageSize);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Post> userWall = PageUtil.pageFromList(pageNo, pageSize, user.getPosts());

        return userWall;
    }

    @Override
    public void addPostToUser(Post post, int userId) {
        log.info("String adding post to user with id {}", userId);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setType(2);
        user.getPosts().add(post);
    }


}
