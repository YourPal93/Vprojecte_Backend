package com.friend.your.vprojecte.service;


import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendService {

    Page<AppUser> findAllFriends(int pageNo, int pageSize, AppUser user);

    AppUser findFriend(String login);

    Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String login);

    AppUser addFriend(AppUser user, int idOfUserToAdd);

    void deleteFriend(AppUser user, int idOfFriend);
}
