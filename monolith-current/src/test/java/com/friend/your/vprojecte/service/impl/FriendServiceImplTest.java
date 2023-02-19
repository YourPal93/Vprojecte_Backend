package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPlateRepository userPlateRepository;
    @InjectMocks
    FriendServiceImpl underTest;

    @Test
    void getUserFriendList_WithValidLogin_ShouldReturnPageOfAppUserPlates() {
        Page<AppUserPlate> testAppUserPlatesPage = mock(Page.class);

        when(userRepository.getFriendList(anyString(), any(Pageable.class))).thenReturn(testAppUserPlatesPage);

        Page<AppUserPlate> returnedPage = underTest.getUserFriendList(0, 10, "TEST_LOGIN");

        assertEquals(testAppUserPlatesPage, returnedPage);
    }

    @Test
    void findFriend_GivenValidUserLoginAndFriendLogin_ShouldReturnAppUserPlate() {
        AppUserPlate testUserPlate = mock(AppUserPlate.class);

        when(userRepository.findFriend(anyString(), anyString())).thenReturn(Optional.of(testUserPlate));

        AppUserPlate returnedPlate = underTest.findFriend(anyString(), anyString());

        assertEquals(returnedPlate, testUserPlate);
    }

    @Test
    void findFriendsMatch_GivenValidUserLoginAndFriendLogin_ShouldReturnPageOfAppUserPlates() {
        Page<AppUserPlate> testUserPlatePage = mock(Page.class);

        when(userRepository.findFriendsMatching(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(testUserPlatePage);

        Page<AppUserPlate> returnedPage = underTest.findFriendsMatch(0, 10, "TEST_LOGIN", "TEST_LOGIN");

        verify(userRepository).findFriendsMatching(anyString(), anyString(),any(Pageable.class));
        assertEquals(testUserPlatePage, returnedPage);
    }

    @Test
    void addFriendToUser_GivenValidUserLoginAndAppUserPlate_ShouldReturnAppUserPlate() {
        AppUser testUser = TestUserUtil.getTestUser();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        AppUserPlate returnedUserPlate = underTest.addFriendToUser("TEST_LOGIN", testUserPlate);

        verify(userRepository).save(any(AppUser.class));
        assertEquals(returnedUserPlate.getUserId(), testUserPlate.getUserId());
    }

    @Test
    void deleteFriend_GivenValidUserLoginAndValidFriendId_ShouldNotReturnAnything() {
        AppUser testUser = TestUserUtil.getTestUser();
        AppUserPlate testUserPlate = mock(AppUserPlate.class);

        when(userPlateRepository.findById(anyInt())).thenReturn(Optional.of(testUserPlate));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        underTest.deleteFriend("TEST_LOGIN", 1);

        verify(userPlateRepository).findById(anyInt());
        verify(userRepository).save(any(AppUser.class));
    }
}