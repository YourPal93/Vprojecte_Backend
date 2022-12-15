package com.friend.your.vprojecte.service.impl;


import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
    public Boolean exist(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public AppUser save(AppUser user) {
        log.info("Saving user '{}' to the database ", user.getLogin());

        if(user.getId() == null) {
            Collection<Role> roles = new ArrayList<>();
            roles.add(new Role("ROLE_USER"));
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting user with id: {}", id);

        userRepository.deleteById(id);
    }


}
