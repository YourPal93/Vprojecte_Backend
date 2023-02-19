package com.friend.your.vprojecte.test_util.dto;

import com.friend.your.vprojecte.dto.AppUserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestUserDtoUtil {

    public static AppUserDto getTestUserDto() {

        AppUserDto testUserDto = getTestUserDtoNoId();

        testUserDto.setId(1);

        return testUserDto;
    }

    public static AppUserDto getTestUserDtoNoId() {

        AppUserDto testUserDto = new AppUserDto();

        testUserDto.setName("TEST_NAME");
        testUserDto.setPassword("TEST_PASSWORD");
        testUserDto.setLogin("TEST_LOGIN");
        testUserDto.setEmail("TEST_EMAIL");
        testUserDto.setBirthdate(LocalDate.of(1111, 11, 11));

        return testUserDto;
    }

    public static List<AppUserDto> getTestUserDtoMultiple(int amount) {
        List<AppUserDto> testUserDtos = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUserDto testUserDto = getTestUserDtoNoId();

            testUserDto.setLogin("TEST_LOGIN" + (i + 1));
            testUserDto.setEmail("TEST_EMAIL" + (i + 1));
            testUserDto.setId(i + 1);
            testUserDtos.add(testUserDto);
        }

        return testUserDtos;
    }

    public static List<AppUserDto> getTestUserDtoMultipleNoId(int amount) {
        List<AppUserDto> testUserDtos = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            AppUserDto testUserDto = getTestUserDtoNoId();

            testUserDto.setLogin("TEST_LOGIN" + (i + 1));
            testUserDto.setEmail("TEST_EMAIL" + (i + 1));
            testUserDtos.add(testUserDto);
        }

        return testUserDtos;
    }
}
