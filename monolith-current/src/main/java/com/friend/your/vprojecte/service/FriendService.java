package com.friend.your.vprojecte.service;


import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendService {

    Page<AppUserPlate> getUserFriendList(int pageNo, int pageSize, String userLogin);

    AppUserPlate findFriend(String userLogin, String friendLogin);

    Page<AppUserPlate> findFriendsMatch(int pageNo, int pageSize, String userLogin, String friendLogin);

    AppUserPlate addFriendToUser(String loginOfUser, AppUserPlate userPlateToAdd);

    void deleteFriend(String userLogin, Integer friendId);
}
