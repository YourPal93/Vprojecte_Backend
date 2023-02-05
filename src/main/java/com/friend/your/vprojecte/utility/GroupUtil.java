package com.friend.your.vprojecte.utility;

import com.friend.your.vprojecte.entity.Role;

public class GroupUtil {

    public static Role getMemberRole(Integer groupId) {
        Role groupRole = new Role("ROLE_GROUP_" + groupId + "_MEMBER");

        groupRole.setRoleType(1);

        return groupRole;
    }
    public static Role getModeratorRole(Integer groupId) {
        Role groupRole = new Role("ROLE_GROUP_" + groupId + "_MODERATOR");

        groupRole.setRoleType(1);

        return groupRole;
    }

    public static Role getAdminRole(Integer groupId) {
        Role groupRole = new Role("ROLE_GROUP_" + groupId + "_ADMIN");

        groupRole.setRoleType(1);

        return groupRole;
    }

    public static String getRoleName(Integer groupId, String rawRoleName) {
        return "ROLE_GROUP_" + groupId + "_" + rawRoleName;
    }

}
