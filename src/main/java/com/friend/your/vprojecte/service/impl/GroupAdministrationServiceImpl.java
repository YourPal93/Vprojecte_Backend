package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.GroupAdministrationService;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.utility.Groups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GroupAdministrationServiceImpl implements GroupAdministrationService {

    private final UserRepository userRepository;
    private final GroupJPARepository groupRepository;
    private final UserPlateJPARepository userPlateRepository;
    private final RoleService roleService;
    private final AddRequestJPARepository requestRepository;
    private final PostRepository postRepository;

    @Override
    public Group createGroup(String loginOfUser, Group group) {
        log.info("Creating group with name {} by user with login {}", group.getName(), loginOfUser);

        AppUser groupAdmin = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUserPlate adminUserPlate = userPlateRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Set<Role> groupRoles = groupAdmin.getGroupRoles();

        groupRoles.add(Groups.getMemberRole(group.getName(), groupAdmin.getId()));
        groupRoles.add(Groups.getModeratorRole(group.getName(), groupAdmin.getId()));
        groupRoles.add(Groups.getAdminRole(group.getName(), groupAdmin.getId()));
        group.setMembers(new HashSet<>());
        group.getMembers().add(adminUserPlate);

        userRepository.save(groupAdmin);
        return groupRepository.save(group);
    }

    @Override
    public Group updateGroup(Group group) {
        log.info("Updating group with id {}" , group.getId());

        Group groupToUpdate = groupRepository.findById(group.getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        groupToUpdate.setName(group.getName());
        groupToUpdate.setClosed(group.isClosed());

        return groupRepository.save(groupToUpdate);
    }

    @Override
    public void deleteGroup(int idOfGroup) {
        log.info("Deleting group with id {}", idOfGroup);

        Group groupToDelete = groupRepository.findById(idOfGroup)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        roleService.deleteRoleAll("ROLE_" + groupToDelete.getName().toUpperCase() + "_ADMIN");
        roleService.deleteRoleAll("ROLE_" + groupToDelete.getName().toUpperCase() + "_MODERATOR");
        roleService.deleteRoleAll("ROLE_" + groupToDelete.getName().toUpperCase() + "_MEMBER");

        groupRepository.delete(groupToDelete);
    }

    @Override
    public void setModerator(String nameOfGroup, int idOfMember) {
        log.info("Setting user with id {} as moderator for the group with name {}", idOfMember, nameOfGroup);

        AppUser member = userRepository.findById(idOfMember)
                .orElseThrow(() -> new RuntimeException("User not found"));

        roleService.addGroupRoleToUser(member, Groups.getModeratorRole(nameOfGroup, member.getId()));
    }

    @Override
    public void deleteModerator(String nameOfGroup, int idOfMember) {
        log.info("Deleting moderator with id {} for the group with name {}", idOfMember, nameOfGroup);

        AppUser member = userRepository.findById(idOfMember)
                .orElseThrow(() -> new RuntimeException("User not found"));

        roleService.deleteRole(Groups.getModeratorRole(nameOfGroup, member.getId()));
    }

    @Override
    public void setAdmin(String loginOfUser, String nameOfGroup, int idOfMember) {
        log.info("Setting user with login {} as admin for the group with name {}", loginOfUser, nameOfGroup);

        AppUser newAdmin = userRepository.findById(idOfMember)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUser groupAdmin = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        roleService.deleteRole(Groups.getAdminRole(nameOfGroup, groupAdmin.getId()));
        roleService.addGroupRoleToUser(newAdmin, (Groups.getAdminRole(nameOfGroup, newAdmin.getId())));
    }

    @Override
    public void deleteMember(int idOfGroup, int idOfMember) {
        log.info("Deleting user with id {} from the gorup with id {}", idOfMember, idOfGroup);

        Group group = groupRepository.findById(idOfGroup)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUserPlate userPlateToDelete = userPlateRepository.findById(idOfMember)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        group.getMembers().remove(userPlateToDelete);

        roleService.deleteRole(Groups.getMemberRole(group.getName(), idOfMember));
        groupRepository.save(group);
    }

    @Override
    public void approveMembershipRequest(AddRequest request) {
        log.info("Approving membership request with id {}", request.getId());

        Group group = groupRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUserPlate userPlateToAdd = userPlateRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Role newMemberRole = Groups.getMemberRole(group.getName(), userPlateToAdd.getUserId());

        group.getMembers().add(userPlateToAdd);

        roleService.addGroupRoleToUser(user, newMemberRole);
        groupRepository.save(group);
        requestRepository.deleteById(request.getId());
    }

    @Override
    public void denyMembershipRequest(AddRequest request) {
        log.info("Denying membership request with id {}", request.getId());

        requestRepository.deleteById(request.getId());
    }


    @Override
    public void deletePost(int idOfPost) {
        log.info("Deleting group post with id {}", idOfPost);

        postRepository.deleteById(idOfPost);
    }
}
