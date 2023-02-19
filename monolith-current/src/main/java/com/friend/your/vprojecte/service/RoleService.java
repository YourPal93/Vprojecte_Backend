package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;

import java.util.Set;

public interface RoleService {


    void addRoleToUser(Integer userId, Role role);

    Role getUserRole(Integer userId, String roleName);

    Set<Role> getUserRoles(Integer userId, Integer roleType);
    void updateUserRole(Role role);

    boolean exists(String roleName, Integer userId);

    void deleteRoleFromUser(String roleName, Integer userId);

    void deleteAllRolesFromGroup(String groupRoleName);
}
