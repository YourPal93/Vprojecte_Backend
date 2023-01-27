package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.FriendRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Friend;
import com.friend.your.vprojecte.service.FriendService;
import com.friend.your.vprojecte.service.UserService;
import com.friend.your.vprojecte.utility.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final UserPlateJPARepository userPlateRepository;

    @Override
    public Page<AppUserPlate> findAllFriends(int pageNo, int pageSize, String login) {
        log.info("Request friend list for user {}", login);

        AppUser user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Friend> friendsPage = friendRepository.findAll(pageable);
        Page<AppUserPlate> returnPage = new PageImpl<>(
                friendsPage.stream().map(Friend::getFriendPlate).toList(),
                pageable,
                friendsPage.getTotalElements()
        );

        return returnPage;
    }

    @Override
    public AppUserPlate findFriend(String loginOfFriend) {
        log.info("Requesting friend {} from the database", loginOfFriend);

        AppUserPlate user = userPlateRepository
                .findByLogin(loginOfFriend).orElseThrow(() -> new RuntimeException("User plate not found"));

        return user;
    }

    @Override
    public Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String login) {
        log.info("Requesting matching user plates for login {} page no {} page size {}", login, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userPlateRepository.findByLoginContaining(login, pageable);
    }

    @Override
    public AppUserPlate addFriend(String loginOfUser, Integer idOfUserToAdd) {
        // TODO:FriendService: addFriend - add support for accept frined request/leave in followers

        AppUserPlate userPlate = userPlateRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Friend newFriend = new Friend(userPlate);

        newFriend.setAdded(true);
        friendRepository.save(newFriend);

        return userPlate;
    }

    @Override
    public void deleteFriend(String loginOfUser, Integer idOfFriend) {
        log.info("Deleting friend {} from user {} friend list", idOfFriend, loginOfUser);

        friendRepository.deleteById(idOfFriend);
    }
}
