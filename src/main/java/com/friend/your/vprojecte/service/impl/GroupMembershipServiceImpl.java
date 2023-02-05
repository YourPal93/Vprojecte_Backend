package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.GroupMembershipService;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.utility.GroupUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final RoleService roleService;


    @Override
    public Page<GroupDto> findAll(int pageNo, int pageSize) {
        log.info("Requesting groups page {} size of page {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return groupRepository.findAllGroupDto(pageable);
    }

    @Override
    public Page<GroupDto> findUserGroups(int pageNo, int pageSize, String userLogin) {
        log.info("Requesting groups of user with login {} page no {} page size {}", userLogin, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return userRepository.findUserGroups(userLogin, pageable);
    }


    @Override
    public Page<GroupDto> findByNameMatch(int pageNo, int pageSize, String name) {
        log.info("Requesting matching groups with name {} page {} page size {}", name, pageNo, pageSize);;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return groupRepository.findAllContainingGroupDto(name, pageable);
    }

    @Override
    public Role joinGroup(Integer groupId, String userLogin) {
        log.info("Adding user with login {} as member to the group with id {}", userLogin, groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        AppUser user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role newGroupRole = GroupUtil.getMemberRole(groupId);

        group.getMembers().add(new AppUserPlate(user.getId(), user.getLogin()));
        groupRepository.save(group);

        roleService.addRoleToUser(user.getId(), newGroupRole);

        user.getGroups().add(group);
        user.getRoles().add(newGroupRole);
        userRepository.save(user);

        return newGroupRole;
    }

    @Override
    public void sendMembershipRequest(AddRequest request) {
        log.info("Sending membership request with id {}", request.getId());

        Group group = groupRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.getJoinRequests().add(request);
        groupRepository.save(group);
    }

    @Override
    public PostDto makePost(Integer groupId, Post post) {
        log.info("Creating group post for the group with id {}", groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        PostDto postDto = new PostDto();

        LocalDateTime currentDateTime = LocalDateTime.now();

        post.setType(1);
        post.setCreationDate(currentDateTime);

        postRepository.save(post);

        group.getPosts().add(post);
        groupRepository.save(group);

        postDto.setId(post.getId());
        postDto.setUserLogin(post.getUserLogin());
        postDto.setDescription(post.getDescription());
        postDto.setUrl(post.getUrl());
        postDto.setCreationDate(post.getCreationDate());

        return postDto;
    }
}
