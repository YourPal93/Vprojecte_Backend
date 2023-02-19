package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.test_util.TestGroupUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupAdministrationServiceImplTest {

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
    @Mock
    private AddRequestRepository addRequestRepository;
    @InjectMocks
    private GroupAdministrationServiceImpl underTest;

    @Test
    void createGroup_GivenValidUserLoginAndValidGroup_ShouldReturnGroupDto() {
        AppUser testUser = mock(AppUser.class);
        Group testGroup = new Group();

        testGroup.setName("TEST_GROUP");

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);
        when(userRepository.save(testUser)).thenReturn(testUser);

        underTest.createGroup("TEST_LOGIN", testGroup);
    }

    @Test
    void updateGroup_GivenValidGroupDto_ShouldReturnGroupDto() {
        GroupDto testGroupDto = mock(GroupDto.class);
        Group testGroup = mock(Group.class);

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        underTest.updateGroup(testGroupDto);

        verify(groupRepository).save(testGroup);
    }

    @Test
    void deleteGroup_GivenValidGroupId_ShouldNotReturnAnything() {
        Group testGroup = new Group();

        testGroup.setName("TEST_NAME");

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        doNothing().when(groupRepository).deleteById(anyInt());
        doNothing().when(roleService).deleteAllRolesFromGroup(anyString());

        underTest.deleteGroup(1);

        verify(groupRepository).deleteById(anyInt());
    }

    @Test
    void setModerator_GivenValidUserId_ShouldNotReturnAnything() {
        AppUser testUser = mock(AppUser.class);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        underTest.setModerator(1, 1);

        verify(userRepository).save(testUser);
    }

    @Test
    void deleteModerator_GivenGroupNameAndValidUserId_ShouldNotReturnAnything() {

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(mock(AppUser.class)));
        when(roleService.getUserRole(anyInt(), anyString())).thenReturn(mock(Role.class));
        when(userRepository.save(any(AppUser.class))).thenReturn(mock(AppUser.class));

        underTest.deleteModerator(1, 1);

        verify(roleService).getUserRole(anyInt(), anyString());
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void setAdmin_GivenValidUserLoginAndValidMemberLoginAndValidGroupName_ShouldNotReturnAnything() {
        AppUser testUser1 = mock(AppUser.class);
        AppUser testUser2 = mock(AppUser.class);

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser1));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser2));
        when(roleService.exists(anyString(), anyInt())).thenReturn(false);
        doNothing().when(roleService).deleteRoleFromUser(anyString(), anyInt());
        doNothing().when(roleService).addRoleToUser(anyInt(), any(Role.class));

        underTest.setAdmin("TEST_LOGIN1", 1, 1);

        verify(roleService, times(2)).addRoleToUser(anyInt(), any(Role.class));
        verify(roleService).deleteRoleFromUser(anyString(), anyInt());
    }

    @Test
    void deleteMember_GivenValidUserIdAndValidGroupName_ShouldNotReturnAnything() {
        Group testGroup = mock(Group.class);
        AppUser testUesr = mock(AppUser.class);

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUesr));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);
        when(roleService.exists(anyString(), anyInt())).thenReturn(false);
        when(roleService.getUserRole(anyInt(), anyString())).thenReturn(mock(Role.class));
        when(userRepository.save(testUesr)).thenReturn(testUesr);

        underTest.deleteMember(1, 1);

        verify(groupRepository).save(testGroup);
        verify(userRepository).save(testUesr);
    }

    @Test
    void approveMembershipRequest_GivenValidAddRequest_ShouldNotReturnAnything() {
        Group testGroup = TestGroupUtil.getTestGroup();
        AppUser testUser = mock(AppUser.class);
        AddRequest testRequest = mock(AddRequest.class);

        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(testGroup));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);
        doNothing().when(addRequestRepository).deleteById(anyInt());

        underTest.approveMembershipRequest(testRequest);

        verify(groupRepository).findById(anyInt());
        verify(userRepository).findById(anyInt());
        verify(groupRepository).save(testGroup);
        verify(userRepository).save(testUser);
        verify(addRequestRepository).deleteById(anyInt());
    }

    @Test
    void denyMembershipRequest_GivenValidAddRequest_ShouldNotReturnAnything() {
        AddRequest testRequest = mock(AddRequest.class);

        doNothing().when(addRequestRepository).deleteById(anyInt());

        underTest.denyMembershipRequest(testRequest);

        verify(addRequestRepository).deleteById(anyInt());
    }

    @Test
    void deletePost_GivenValidPostId_ShouldNotReturnAnything() {

        doNothing().when(postRepository).deleteById(anyInt());

        underTest.deletePost(1);

        verify(postRepository).deleteById(1);
    }
}