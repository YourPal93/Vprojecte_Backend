package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.GroupAdministrationService;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.utility.GroupUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GroupAdministrationServiceImpl implements GroupAdministrationService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserPlateRepository userPlateRepository;
    private final RoleService roleService;
    private final AddRequestRepository requestRepository;
    private final PostRepository postRepository;

    @Override
    public Group createGroup(String userLogin, Group group) {
        log.info("Creating group with name {} by user with login {}", group.getName(), userLogin);

        AppUser user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        group.setMembers(new HashSet<>());
        group.getMembers().add(new AppUserPlate(user.getId(), user.getLogin()));

        groupRepository.save(group);

        Role newAdminRole = GroupUtil.getAdminRole(group.getId());

        user.getRoles().add(newAdminRole);
        user.getGroups().add(group);
        userRepository.save(user);

        return group;
    }

    @Override
    public GroupDto updateGroup(GroupDto group) {
        log.info("Updating group with id {}" , group.getId());

        Group groupToUpdate = groupRepository.findById(group.getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        groupToUpdate.setName(group.getName());
        groupToUpdate.setClosed(group.getClosed());

        groupRepository.save(groupToUpdate);

        return group;
    }

    @Override
    public void deleteGroup(Integer groupId) {
        log.info("Deleting group with id {}", groupId);

        Group groupToDelete = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        String adminRole = GroupUtil.getRoleName(groupId, "ADMIN");
        String moderatorRole = GroupUtil.getRoleName(groupId, "MODERATOR");
        String memberRole = GroupUtil.getRoleName(groupId, "MEMBER");

        roleService.deleteAllRolesFromGroup(adminRole);
        roleService.deleteAllRolesFromGroup(moderatorRole);
        roleService.deleteAllRolesFromGroup(memberRole);

        groupRepository.deleteById(groupId);
    }

    @Override
    public void setModerator(Integer groupId, Integer memberId) {
        log.info("Setting user with id {} as moderator for the group with id {}", memberId, groupId);

        AppUser member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role moderatorRole = GroupUtil.getModeratorRole(groupId);
        Role memberRole = GroupUtil.getMemberRole(groupId);

        if(roleService.exists(memberRole.getName(), member.getId())) {
            Role existingMemberRole = roleService.getUserRole(memberId, memberRole.getName());
            member.getRoles().remove(existingMemberRole);
        }

        member.getRoles().add(moderatorRole);
        userRepository.save(member);
    }

    @Override
    public void deleteModerator(Integer groupId, Integer memberId) {
        log.info("Deleting moderator with id {} for the group with id {}", memberId, groupId);

        String moderatorRoleName = GroupUtil.getRoleName(groupId, "MODERATOR");

        AppUser member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role existingModeratorRole = roleService.getUserRole(memberId, moderatorRoleName);

        member.getRoles().remove(existingModeratorRole);
        member.getRoles().add(GroupUtil.getMemberRole(groupId));
        userRepository.save(member);
    }

    @Override
    public void setAdmin(String userLogin, Integer memberId, Integer groupId) {
        log.info("Setting user with id {} as admin for the group with id {}", memberId, groupId);

        AppUser newAdmin = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUser formerAdmin = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role memberRole = GroupUtil.getMemberRole(groupId);
        Role moderatorRole = GroupUtil.getModeratorRole(groupId);
        Role adminRole = GroupUtil.getAdminRole(groupId);

        if(roleService.exists(memberRole.getName(), newAdmin.getId())) {
            roleService.deleteRoleFromUser(memberRole.getName(), newAdmin.getId());
        }

        if(roleService.exists(moderatorRole.getName(), newAdmin.getId())) {
            roleService.deleteRoleFromUser(moderatorRole.getName(), newAdmin.getId());
        }

        roleService.deleteRoleFromUser(adminRole.getName(), formerAdmin.getId());
        roleService.addRoleToUser(formerAdmin.getId(), moderatorRole);
        roleService.addRoleToUser(newAdmin.getId(), adminRole);
    }

    @Override
    public void deleteMember(Integer memberId, Integer groupId) {
        log.info("Deleting user with id {} from the group with id {}", memberId, groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUser member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        group.getMembers().remove(new AppUserPlate(memberId, member.getLogin()));
        groupRepository.save(group);

        String memberRoleName = GroupUtil.getRoleName(groupId, "MEMBER");
        String moderatorRoleName = GroupUtil.getRoleName(groupId, "MODERATOR");

        if(roleService.exists(moderatorRoleName, member.getId())) {
            Role existingModeratorRole = roleService.getUserRole(memberId, moderatorRoleName);

            member.getRoles().remove(existingModeratorRole);
            userRepository.save(member);
            return;
        }

        Role existingMemberRole = roleService.getUserRole(memberId, moderatorRoleName);
        member.getRoles().remove(existingMemberRole);
        userRepository.save(member);
    }

    @Override
    public void approveMembershipRequest(AddRequest request) {
        log.info("Approving membership request with id {}", request.getId());

        Group group = groupRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role memberRole = GroupUtil.getMemberRole(group.getId());

        user.getRoles().add(memberRole);
        group.getMembers().add(new AppUserPlate(user.getId(), user.getLogin()));

        userRepository.save(user);
        groupRepository.save(group);
        requestRepository.deleteById(request.getId());
    }

    @Override
    public void denyMembershipRequest(AddRequest request) {
        log.info("Denying membership request with id {}", request.getId());

        requestRepository.deleteById(request.getId());
    }


    @Override
    public void deletePost(Integer postId) {
        log.info("Deleting group post with id {}", postId);

        postRepository.deleteById(postId);
    }
}
