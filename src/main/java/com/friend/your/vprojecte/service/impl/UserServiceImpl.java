package com.friend.your.vprojecte.service.impl;


import com.friend.your.vprojecte.dao.RoleRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public AppUser findById(int id) {
        log.info("Requesting user with id: {}", id);

        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }

    @Override
    public AppUser findByLogin(String login) {
        log.info("Requesting user with login: {}", login);

        AppUser user = userRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        return user;
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
    public AppUser save(AppUser user) {
        log.info("Saving user '{}' to the database ", user.getLogin());

        if(user.getId() == null) {
            user.setRoles(new HashSet<>());
            roleService.addRoleToUser(user, new Role("ROLE_USER", user.getId()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        AppUser savedUser = userRepository.save(user);
        AppUserPlate savedUserPlate = new AppUserPlate(savedUser.getId(), savedUser.getLogin());

        userPlateRepository.save(savedUserPlate);

        return savedUser;
    }

    @Override
    public void delete(int id) {
        log.info("Deleting user with id: {}", id);

        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
        userPlateRepository.deleteById(user.getId());
    }

    @Override
    public Page<Chat> getChatLogs(int pageNo, int pageSize, AppUser user) {
        log.info("Requesting chat logs of user {}", user.getLogin());

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Chat> pageOfChatLogs = new PageImpl<>(user.getChatLog(), pageable, user.getChatLog().size());

        return pageOfChatLogs;
    }


}
