package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;

public interface RoleService {

    public Role saveRole(Role role);

    public void deleteRole(Role role);

    public void deleteRoleAll(String roleName);

    public AppUser addRoleToUser(AppUser user, Role role);
    public AppUser addGroupRoleToUser(AppUser user, Role role);
}
