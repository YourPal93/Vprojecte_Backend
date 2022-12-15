package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dao.FriendRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Friend;
import com.friend.your.vprojecte.service.impl.FriendServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendRepository friendRepository;
    @InjectMocks
    private FriendServiceImpl friendService;

    @Test
    public void FriendService_FindAllFriend_ReturnPageOfAppUsers() {
        Page<AppUser> mockFriends = Mockito.mock(Page.class);
        AppUser mockUser = Mockito.mock(AppUser.class);


        when(userRepository.findByIdIn(Mockito.any(Collection.class), Mockito.any(Pageable.class)))
                .thenReturn(mockFriends);

        Page<AppUser> friends = friendService.findAllFriends(1, 10, mockUser);

        Assertions.assertThat(friends).isNotNull();
    }

    @Test
    public void FriendService_FindFriend_ReturnAppUser() {
        AppUser mockUser = Mockito.mock(AppUser.class);

        when(userRepository.findByLogin("Test")).thenReturn(Optional.ofNullable(mockUser));

        AppUser friendByLogin = friendService.findFriend("Test");

        Assertions.assertThat(friendByLogin).isNotNull();
    }

    @Test
    public void FriendService_AddFriend_ReturnAppUser() {
        AppUser mockUser = new AppUser(
                "AppUser",
                "1234",
                "Test",
                "something@proba.test",
                LocalDate.of(1111, 11, 11));
        mockUser.setFriendList(new HashSet<>());

        when(userRepository.save(Mockito.any(AppUser.class))).thenReturn(mockUser);

        AppUser userWithFriend = friendService.addFriend(mockUser, 1);
        Friend fakeFriend = new Friend(1, true);

        Assertions.assertThat(userWithFriend).isNotNull();
        Assertions.assertThat(userWithFriend.getFriendList().contains(fakeFriend)).isTrue();
    }

    @Test
    public void FriendService_DeleteFriend_ReturnAppUser() {
        AppUser mockUser = new AppUser(
                "AppUser",
                "1234",
                "Test",
                "something@proba.test",
                LocalDate.of(1111, 11, 11));
        Friend mockFriend = new Friend(1, true);
        mockFriend.setId(1);

        mockUser.setFriendList(new HashSet<>());
        mockUser.getFriendList().add(mockFriend);

        when(friendRepository.findById(1)).thenReturn(Optional.ofNullable(mockFriend));
        when(userRepository.save(Mockito.any(AppUser.class))).thenReturn(mockUser);

        friendService.deleteFriend(mockUser, 1);

        Assertions.assertThat(mockUser.getFriendList().contains(mockFriend)).isFalse();
    }
}
