package com.friend.your.vprojecte.service;


import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendService {

    Page<AppUserPlate> findAllFriends(int pageNo, int pageSize, String login);

    AppUserPlate findFriend(String login);

    Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String login);

    AppUserPlate addFriend(String loginOfUser, Integer idOfUserToAdd);

    void deleteFriend(String loginOfUser, Integer idOfFriend);
}
