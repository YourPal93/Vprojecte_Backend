package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class TestUserPlateUtil {
    public static AppUserPlate getTestUserPlate() {
        AppUserPlate testUserPlate = getTestUserPlateNoId();

        testUserPlate.setUserId(1);

        return testUserPlate;
    }

    public static AppUserPlate getTestUserPlateNoId() {
        AppUserPlate testUserPlate = new AppUserPlate();

        testUserPlate.setUserLogin("TEST_LOGIN");

        return testUserPlate;
    }

    public static List<AppUserPlate> getTestUserPlatesMultiple(int amount) {
        List<AppUserPlate> testUserPlates = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUserPlate testUserPlate = getTestUserPlateNoId();

            testUserPlate.setUserLogin("TEST_LOGIN" + i + 1);
            testUserPlate.setUserId(i + 1);
            testUserPlates.add(testUserPlate);
        }

        return testUserPlates;
    }

    public static List<AppUserPlate> getTestUserPlatesMultipleNoId(int amount) {
        List<AppUserPlate> testUserPlates = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUserPlate testUserPlate = getTestUserPlateNoId();

            testUserPlate.setUserLogin("TEST_LOGIN" + i + 1);
            testUserPlates.add(testUserPlate);
        }

        return testUserPlates;
    }
}
