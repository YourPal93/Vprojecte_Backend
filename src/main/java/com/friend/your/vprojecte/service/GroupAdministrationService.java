package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.*;

public interface GroupAdministrationService {

    Group createGroup(String loginOfUser, Group group);

    Group updateGroup(Group group);

    void deleteGroup(int idOfGroup);

    void setModerator(String nameOfGroup, int idOfMember);

    void deleteModerator(String nameOfGroup, int idOfMember);

    void setAdmin(String loginOfUser, String nameOfGroup, int idOfMember);

    void deleteMember(int idOfGroup, int idOfMember);

    void approveMembershipRequest(AddRequest request);

    void denyMembershipRequest(AddRequest request);

    void deletePost(int idOfPost);

}
