package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.AddRequest;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;

public interface GroupMembershipService {

    Page<Group> findAll(int pageNo, int pageSize);

    Group findGroup(int idOfGroup);

    Page<Group> findByNameMatch(int pageNo, int pageSize, String name);

    void addMember(int idOfGroup, String loginOfUser);

    Page<AppUser> findMember(int pageNo, int pageSize, String loginOfMember);

    void sendMembershipRequest(AddRequest request);

    void makePost(int idOfGroup, Post post);
}
