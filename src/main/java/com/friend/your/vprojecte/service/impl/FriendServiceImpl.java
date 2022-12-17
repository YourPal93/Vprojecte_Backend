package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.FriendRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
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

    @Override
    public Page<AppUser> findAllFriends(int pageNo, int pageSize, AppUser user) {
        log.info("Request friend list for user {}", user.getLogin());

        List<Integer> friendIds = new ArrayList<>();
        friendIds.addAll(user.getFriendList().stream().map(Friend::getFriendId).toList());
        Pageable pageable = PageRequest.of(pageNo, pageSize);

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
    public AppUser addFriend(AppUser user, int idOfUserToAdd) {
        // TODO: FriendService: addFriend - add support for accept frined request/leave in followers
        log.info("Adding friend {} to user {}", idOfUserToAdd, user.getLogin());

        Friend friendToAdd = new Friend(idOfUserToAdd, true);
        user.getFriendList().add(friendToAdd);
        return userRepository.save(user);
    }

    @Override
    public void deleteFriend(AppUser user, int idOfFriend) {
        log.info("Deleting friend {} from user {} friend list", idOfFriend, user.getLogin());

        Optional<Friend> friend = friendRepository.findById(idOfFriend);
        if(friend.isPresent()) {
            user.getFriendList().remove(friend.get());
            userRepository.save(user);
        }
    }
}
