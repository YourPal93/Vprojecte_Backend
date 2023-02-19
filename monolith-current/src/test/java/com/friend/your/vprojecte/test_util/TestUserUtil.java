package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestUserUtil {

    public static AppUser getTestUser() {
        AppUser testUser = getTestUserNoId();

        testUser.setId(1);

        return testUser;
    }

    public static AppUser getTestUserNoId() {
        AppUser testUser = new AppUser(
                "TEST_NAME",
                "TEST_PASSWORD",
                "TEST_LOGIN",
                "TEST_EMAIL",
                LocalDate.of(2000, 1, 1)
        );

        testUser.setRoles(new HashSet<>());
        testUser.setFriendList(new HashSet<>());
        testUser.setChatLog(new ArrayList<>());
        testUser.setPosts(new ArrayList<>());
        testUser.setGroups(new HashSet<>());

        Role testUserRole = new Role("ROLE_USER");

        testUser.getRoles().add(testUserRole);

        return testUser;
    }


    public static List<AppUser> getTestUsersMultiple(int amount) {
        List<AppUser> testUsers = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUser testUser = getTestUserNoId();

            testUser.setLogin("TEST_LOGIN" + (i + 1));
            testUser.setEmail("TEST_EMAIL" + (i + 1));
            testUser.setId(i + 1);
            testUsers.add(testUser);
        }

        return testUsers;
    }

    public static List<AppUser> getTestUsersMultipleNoId(int amount) {
        List<AppUser> testUsers = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUser testUser = getTestUserNoId();

            testUser.setLogin("TEST_LOGIN" + (i + 1));
            testUser.setEmail("TEST_EMAIL" + (i + 1));
            testUsers.add(testUser);
        }

        return testUsers;
    }
}
