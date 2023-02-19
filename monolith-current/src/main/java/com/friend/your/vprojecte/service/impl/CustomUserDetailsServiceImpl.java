package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.enums.RoleTypes;
import com.friend.your.vprojecte.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user details from user with username {}", username);


        AppUser user = userRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("Invalid user details"));
        Set<Role> userRoles = roleService.getUserRoles(user.getId(), RoleTypes.ROLE.numericValue);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userRoles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}
