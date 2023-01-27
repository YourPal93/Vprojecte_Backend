package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import org.springframework.data.domain.Page;

public interface GroupMembershipService {

    Page<Group> findAll(int pageNo, int pageSize);

    Group findGroup(Integer idOfGroup);

    Page<Group> findByNameMatch(int pageNo, int pageSize, String name);

    AppUserPlate addMember(Integer idOfGroup, String loginOfUser);

    Page<AppUser> findMember(int pageNo, int pageSize, String loginOfMember);

    AddRequest sendMembershipRequest(AddRequest request);

    PostDto makePost(Integer idOfGroup, Post post);
}
