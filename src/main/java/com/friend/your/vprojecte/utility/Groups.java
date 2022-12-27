package com.friend.your.vprojecte.utility;

import com.friend.your.vprojecte.entity.Role;

public class Groups {

    public static Role getMemberRole(String groupName, int userId) {
        String newString = groupName.toUpperCase().trim().replace(' ', '_');

        return new Role("ROLE_" + newString + "_MEMBER", userId, 1);
    }
    public static Role getModeratorRole(String groupName, int userId) {
        String newString = groupName.toUpperCase().trim().replace(' ', '_');

        return new Role("ROLE_" + newString + "_MODERATOR", userId,1);
    }

    public static Role getAdminRole(String groupName, int userId) {
        String newString = groupName.toUpperCase().trim().replace(' ', '_');

        return new Role("ROLE_" + newString + "_ADMIN", userId, 1);
    }
}
