package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.RoleRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role: {}", role.getName());

        return roleRepository.save(role);
    }
    @Override
    public void deleteRole(AppUser user, Role role) {
        log.info("Deleting role {} from user {}", role.getName(), user.getLogin());

        user.getRoles().remove(role);
    }
    @Override
    public AppUser addRoleToUser(AppUser user, String roleName) {
        log.info("Adding role '{}' to user '{}'", roleName, user.getLogin());

        user.getRoles().add(new Role(roleName));
        return userRepository.save(user);
    }
}
