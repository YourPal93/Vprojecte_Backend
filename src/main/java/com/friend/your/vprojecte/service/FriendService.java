package com.friend.your.vprojecte.service;


import com.friend.your.vprojecte.entity.AppUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FriendService {

    public Page<AppUser> findAllFriends(int pageNo, int pageSize, AppUser user);

    public AppUser findFriend(String login);

    public AppUser addFriend(AppUser user, int idOfUserToAdd);

    public void deleteFriend(AppUser user, int idOfFriend);
}
