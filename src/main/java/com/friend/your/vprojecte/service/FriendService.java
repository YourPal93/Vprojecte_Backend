package com.friend.your.vprojecte.service;


import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendService {

    Page<AppUser> findAllFriends(int pageNo, int pageSize, String login);

    AppUser findFriend(String login);

    Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String login);

    AppUser addFriend(String loginOfUser, int idOfUserToAdd);

    void deleteFriend(String loginOfUser, int idOfFriend);
}
