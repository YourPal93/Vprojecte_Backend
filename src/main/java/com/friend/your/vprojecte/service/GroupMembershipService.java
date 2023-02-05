package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import org.springframework.data.domain.Page;

public interface GroupMembershipService {

    Page<GroupDto> findAll(int pageNo, int pageSize);

    Page<GroupDto> findUserGroups(int pageNo, int pageSize, String userLogin);

    Page<GroupDto> findByNameMatch(int pageNo, int pageSize, String name);

    Role joinGroup(Integer groupId, String userLogin);

    void sendMembershipRequest(AddRequest request);

    PostDto makePost(Integer groupId, Post post);
}
