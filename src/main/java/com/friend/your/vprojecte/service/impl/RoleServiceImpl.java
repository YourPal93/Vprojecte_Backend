package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.RoleRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.enums.RoleTypes;
import com.friend.your.vprojecte.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public void addRoleToUser(Integer userId, Role role) {
        log.info("Adding role '{}' to user '{}'", role.getName(), userId);

        role.setUserId(userId);

        roleRepository.save(role);
    }

    @Override
    public Role getUserRole(Integer userId, String roleName) {
        log.info("Requesting user role with name {} for user with id {}", roleName, userId);

        return roleRepository.findByNameAndUserId(roleName, userId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Set<Role> getUserRoles(Integer userId, Integer roleType) {
        log.info("Requesting user roles of type {} from user with id {}", roleType, userId);

        return roleRepository.findByUserIdAndRoleType(userId, roleType);
    }
    @Override
    public void updateUserRole(Role role) {
        log.info("Updating role with name {} of user with id {}", role.getName(), role.getUserId());

        roleRepository.save(role);
    }

    @Override
    public boolean exists(String roleName, Integer userId) {
        log.info("Checking if role with name {} and user Id {} exists", roleName, userId);

        return roleRepository.existsByNameAndUserId(roleName, userId);
    }


    @Override
    public void deleteRoleFromUser(String roleName, Integer userId) {
        log.info("Deleting role with name {} from user with id {}", roleName, userId);

        roleRepository.deleteByNameAndUserId(roleName, userId);
    }

    @Override
    public void deleteAllRolesFromGroup(String groupRoleName) {
        log.info("Deleting group roles with name {}", groupRoleName);

        roleRepository.deleteByName(groupRoleName);
    }
}
