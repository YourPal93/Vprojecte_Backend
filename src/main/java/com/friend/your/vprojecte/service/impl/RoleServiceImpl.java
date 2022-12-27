package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.RoleRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteRole(Role role) {
        log.info("Deleting role {} from user {}", role.getName(), role.getUserId());

        roleRepository.deleteByNameAndUserId(role.getName(), role.getUserId());
    }

    @Override
    public void deleteRoleAll(String roleName) {
        log.info("Deleting roles {}", roleName);

        roleRepository.deleteByName(roleName);
    }
    @Override
    public AppUser addRoleToUser(AppUser user, Role role) {
        log.info("Adding role '{}' to user '{}'", role.getName(), user.getLogin());

        user.getRoles().add(role);

        return userRepository.save(user);
    }

    @Override
    public AppUser addGroupRoleToUser(AppUser user, Role role) {
        log.info("Adding group  role '{}' to user '{}'", role.getName(), user.getLogin());

        role.setType(1);
        user.getGroupRoles().add(role);

        return userRepository.save(user);
    }
}
