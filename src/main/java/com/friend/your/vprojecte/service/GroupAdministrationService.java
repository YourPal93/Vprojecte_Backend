package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.*;

public interface GroupAdministrationService {

    Group createGroup(String userLogin, Group group);

    public GroupDto updateGroup(GroupDto group);

    void deleteGroup(Integer groupId);

    void setModerator(Integer groupId, Integer memberId);

    void deleteModerator(Integer groupId, Integer memberId);

    void setAdmin(String userLogin, Integer memberId, Integer groupId);

    void deleteMember(Integer userId, Integer groupId);

    void approveMembershipRequest(AddRequest request);

    void denyMembershipRequest(AddRequest request);

    void deletePost(Integer postId);

}
