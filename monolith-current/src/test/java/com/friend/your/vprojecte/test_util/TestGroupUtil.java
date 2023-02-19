package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.Group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestGroupUtil {

    public static Group getTestGroupNoId() {
        Group testGroup = new Group();

        testGroup.setName("TEST_GROUP");
        testGroup.setMembers(new HashSet<>());
        testGroup.setPosts(new ArrayList<>());
        testGroup.setJoinRequests(new HashSet<>());

        return testGroup;
    }

    public static Group getTestGroup() {
        Group testGroup = getTestGroupNoId();

        testGroup.setId(1);

        return testGroup;
    }

    public static List<Group> getTestGroupMultipleNoId(int amount) {
        List<Group> testGroups = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Group testGroup = getTestGroupNoId();

            testGroup.setName("TEST_GROUP" + (i + 1));
            testGroups.add(testGroup);
        }

        return testGroups;
    }

    public static List<Group> getTestGroupMultiple(int amount) {
        List<Group> testGroups = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Group testGroup = getTestGroupNoId();

            testGroup.setId(i + 1);
            testGroup.setName("TEST_GROUP" + (i + 1));
            testGroups.add(testGroup);
        }

        return testGroups;
    }


}
