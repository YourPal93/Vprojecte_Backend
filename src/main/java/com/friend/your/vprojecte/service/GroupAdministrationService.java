package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.*;

public interface GroupAdministrationService {

    Group createGroup(String loginOfUser, Group group);

    public GroupDto updateGroup(GroupDto group);

    void deleteGroup(Integer idOfGroup);

    void setModerator(String nameOfGroup, Integer idOfMember);

    void deleteModerator(String nameOfGroup, Integer idOfMember);

    void setAdmin(String loginOfUser, String nameOfGroup, Integer idOfMember);

    void deleteMember(Integer idOfGroup, Integer idOfMember);

    AppUserPlate approveMembershipRequest(AddRequest request);

    void denyMembershipRequest(AddRequest request);

    void deletePost(Integer idOfPost);

}
