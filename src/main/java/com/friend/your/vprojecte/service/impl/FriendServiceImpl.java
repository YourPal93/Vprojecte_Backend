package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final UserPlateRepository userPlateRepository;

    @Override
    public Page<AppUserPlate> getUserFriendList(int pageNo, int pageSize, String userLogin) {
        log.info("Request friend list for user with login {} page {} page size {}", userLogin, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userRepository.getFriendList(userLogin, pageable);
    }

    @Override
    public AppUserPlate findFriend(String userLogin, String friendLogin) {
        log.info("Requesting friend {} from of user with login {} the database", friendLogin , userLogin);

        AppUserPlate friendPlate = userRepository.findFriend(userLogin, friendLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        return friendPlate;
    }

    @Override
    public Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String userLogin, String friendLogin) {
        log.info("Requesting matching friend user plates of login {} " +
                "for user with login {} page no {} page size {}", friendLogin, userLogin, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userRepository.findFriendsMatching(userLogin, friendLogin, pageable);
    }

    @Override
    public AppUserPlate addFriendToUser(String loginOfUser, AppUserPlate userPlateToAdd) {
        // TODO:FriendService: addFriend - add support for accept friend request/leave in followers

        AppUser user = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getFriendList().add(userPlateToAdd);
        userRepository.save(user);

        return userPlateToAdd;
    }

    @Override
    public void deleteFriend(String userLogin, Integer friendId) {
        log.info("Deleting friend {} from user {} friend list", friendId, userLogin);

        AppUser user = userRepository.findByLogin(userLogin)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        AppUserPlate friendPlate = userPlateRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        user.getFriendList().remove(friendPlate);
        userRepository.save(user);
    }
}
