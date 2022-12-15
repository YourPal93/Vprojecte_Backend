package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;

public interface RoleService {

    public Role saveRole(Role role);

    public void deleteRole(AppUser user, Role role);

    public AppUser addRoleToUser(AppUser user, String roleName);
}
