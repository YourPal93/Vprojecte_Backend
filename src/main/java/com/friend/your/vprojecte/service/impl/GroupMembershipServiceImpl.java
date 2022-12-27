package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.GroupMembershipService;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.utility.Groups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupJPARepository groupRepository;
    private final UserPlateJPARepository userPlateRepository;
    private final UserRepository userRepository;
    private final AddRequestJPARepository requestRepository;
    private final RoleService roleService;


    @Override
    public Page<Group> findAll(int pageNo, int pageSize) {
        log.info("Requesting groups page {} size of page {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return groupRepository.findAll(pageable);
    }

    @Override
    public Group findGroup(int idOfGroup) {
        log.info("Requesting group with id {}", idOfGroup);

        Group group = groupRepository.findById(idOfGroup).orElseThrow(() -> new RuntimeException("Group not found"));

        return group;
    }


    @Override
    public Page<Group> findByNameMatch(int pageNo, int pageSize, String name) {
        log.info("Requesting matching groups with name {} page {} page size {}", name, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return groupRepository.findByNameContaining(name, pageable);
    }

    @Override
    public void addMember(int idOfGroup, String loginOfUser) {
        log.info("Adding user user with login {} as member to the group with id {}", loginOfUser, idOfGroup);

        Group group = groupRepository.findById(idOfGroup)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUser user = userRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUserPlate userPlate = userPlateRepository.findByLogin(loginOfUser)
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Role newMemberRole = Groups.getMemberRole(group.getName(), userPlate.getUserId());

        group.getMembers().add(userPlate);

        roleService.addGroupRoleToUser(user,newMemberRole);
        groupRepository.save(group);
    }

    @Override
    public Page<AppUser> findMember(int pageNo, int PageSize, String loginOfMember) {
        return null;
    }

    @Override
    public void sendMembershipRequest(AddRequest request) {
        log.info("Sending membership request with id {}", request.getId());

        requestRepository.save(request);
    }

    @Override
    public void makePost(int idOfGroup, Post post) {
        log.info("Creating group post with id {} for the group with id {}", post.getId(), idOfGroup);

        Group group = groupRepository.findById(idOfGroup)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        post.setType(1);
        group.getPosts().add(post);
    }
}
