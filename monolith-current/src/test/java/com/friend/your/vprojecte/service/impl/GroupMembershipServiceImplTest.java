package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.GroupRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupMembershipServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPlateRepository userPlateRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private GroupMembershipServiceImpl underTest;

    @Test
    void findAll_GivenValidPageNoAndPageSize_ShouldReturnPageOfGroupDtos() {
        Page<GroupDto> testGroupsPage = mock(Page.class);

        when(groupRepository.findAllGroupDto(any(Pageable.class))).thenReturn(testGroupsPage);

        underTest.findAll(0, 10);

        verify(groupRepository).findAllGroupDto(any(Pageable.class));
    }

    @Test
    void findByNameMatch_GivenValidPageNoAndPageSize_ShouldReturnPageOfGroupDtos() {
        Page<GroupDto> testGroupDtosPage = mock(Page.class);

        when(groupRepository.findAllContainingGroupDto(anyString(), any(Pageable.class))).thenReturn(mock(Page.class));

        underTest.findByNameMatch(0, 10, "TEST_GROUP");

        verify(groupRepository).findAllContainingGroupDto(anyString(), any(Pageable.class));
    }

    @Test
    void getUserGroups_GivenValidUserName_ShouldReturnPageOfGroupDtos() {
        Page<GroupDto> testGroupDtosPage = mock(Page.class);

        when(userRepository.findUserGroups(anyString(), any(Pageable.class))).thenReturn(testGroupDtosPage);

        Page<GroupDto> returnedPage = underTest.findUserGroups(0, 10, "TEST_LOGIN");

        assertEquals(testGroupDtosPage, returnedPage);
    }


    @Test
    void joinGroup_GivenValidGroupNameAndValidAppUserPlate_ShouldReturnAppUserPlate() {
        Group testGroup = mock(Group.class);
        AppUser testUser = mock(AppUser.class);
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        Role returnedRole = underTest.joinGroup(1, "TEST_LOGIN");

        verify(userRepository).save(testUser);
        verify(groupRepository).save(testGroup);
    }

    @Test
    void sendMembershipRequest_AndValidAddRequest_ShouldNotReturnAnything() {
        AddRequest testRequest = mock(AddRequest.class);
        Group testGroup = mock(Group.class);

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        underTest.sendMembershipRequest(testRequest);

        verify(groupRepository).save(testGroup);
    }

    @Test
    void makePost_GivenValidGroupNameAndValidPost_ShouldReturnPostDto() {
        PostDto testPostDto = mock(PostDto.class);
        Group testGroup = mock(Group.class);
        Post testPost = mock(Post.class);

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        underTest.makePost(1, testPost);

        verify(postRepository).save(any(Post.class));
        verify(groupRepository).save(any(Group.class));
    }
}