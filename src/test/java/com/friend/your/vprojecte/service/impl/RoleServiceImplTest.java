package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.RoleRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RoleServiceImpl underTest;

    @Test
    void addRoleToUser_GivenValidUserIdAndValiRole_ShouldNotReturnAnything() {
        Role testRole = mock(Role.class);

        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        underTest.addRoleToUser(1, testRole);

        verify(roleRepository).save(testRole);
    }

    @Test
    void getUserRoles_givenValidUserIdAndValidRoleType_ShouldReturnSetOfRoles() {
        Set<Role> testRoles = mock(Set.class);

        when(roleRepository.findByUserIdAndRoleType(anyInt(), anyInt())).thenReturn(testRoles);

        Set<Role> returnedRoles = underTest.getUserRoles(1, 1);

        assertEquals(testRoles, returnedRoles);
    }

    @Test
    void updateUserRole_GivenValidRole_ShouldNotReturnAnything() {
        Role testRole = mock(Role.class);

        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        underTest.updateUserRole(testRole);

        verify(roleRepository).save(testRole);
    }

    @Test
    void deleteRoleFromUser_GivenValidRoleNameAndValidAppUserid_ShouldNotReturnAnything() {

        doNothing().when(roleRepository).deleteByNameAndUserId(anyString(), anyInt());

        underTest.deleteRoleFromUser("TEST_ROLE_NAME", 1);

        verify(roleRepository).deleteByNameAndUserId(anyString(), anyInt());
    }

    @Test
    void deleteAllRolesFromGroup_GivenValidRoleName_ShouldNotReturnAnything() {

        doNothing().when(roleRepository).deleteByName(anyString());

        underTest.deleteAllRolesFromGroup("TEST_ROLE_NAME");

        verify(roleRepository).deleteByName(anyString());
    }


}