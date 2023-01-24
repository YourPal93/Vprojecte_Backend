package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.FriendRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Friend;
import com.friend.your.vprojecte.service.FriendService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    private final UserPlateJPARepository userPlateRepository;

    @Override
    public Page<AppUser> findAllFriends(int pageNo, int pageSize, String login) {
        log.info("Request friend list for user {}", login);

        AppUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Integer> friendIds = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        friendIds.addAll(user.getFriendList().stream().map(Friend::getFriendId).toList());

        return userRepository.findByIdIn(friendIds, pageable);
    }

    @Override
    public AppUser findFriend(String loginOfFriend) {
        log.info("Requesting friend {} from the database", loginOfFriend);

        AppUser user = userRepository
                .findByLogin(loginOfFriend).orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }

    @Override
    public Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String login) {
        log.info("Requesting matching user plates for login {} page no {} page size {}", login, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userPlateRepository.findByLoginContaining(login, pageable);
    }

    @Override
    public AppUser addFriend(String loginOfUser, int idOfUserToAdd) {
        // TODO:FriendService: addFriend - add support for accept frined request/leave in followers
        log.info("Adding friend {} to user {}", idOfUserToAdd, loginOfUser);

        AppUser user = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Friend friendToAdd = new Friend(idOfUserToAdd, true);

        user.getFriendList().add(friendToAdd);

        return userRepository.save(user);
    }

    @Override
    public void deleteFriend(String loginOfUser, int idOfFriend) {
        log.info("Deleting friend {} from user {} friend list", idOfFriend, loginOfUser);

        AppUser user = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Friend friend = friendRepository.findById(idOfFriend)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getFriendList().remove(friend);
        userRepository.save(user);
    }
}
