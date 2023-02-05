package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Role;
import com.friend.your.vprojecte.service.RoleService;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private CustomUserDetailsServiceImpl underTest;


    @Test
    void loadUserByUsername_WithLoginForExistingUser_ShouldReturnUserDetails() {
        String userLogin = "TEST_LOGIN";
        AppUser testUser = TestUserUtil.getTestUser();
        Set<Role> testRoles = new HashSet<>();
        SimpleGrantedAuthority testAuthority = new SimpleGrantedAuthority("ROLE_USER");

        testRoles.add(new Role("ROLE_USER"));

        when(userRepository.findByLogin(userLogin)).thenReturn(Optional.of(testUser));
        when(roleService.getUserRoles(anyInt(), anyInt())).thenReturn(testRoles);

        UserDetails userDetails = underTest.loadUserByUsername(userLogin);

        verify(userRepository).findByLogin(userLogin);
        assertEquals(userDetails.getUsername(), testUser.getLogin());
        assertEquals(userDetails.getPassword(), testUser.getPassword());
        assertEquals(userDetails.getAuthorities().size(), testUser.getRoles().size());
        assertTrue(userDetails.getAuthorities().contains(testAuthority));
    }

    @Test
    void loadUserByUsername_WithLoginForNonExistentUser_ShouldThrowException() {
        String userLogin = "TEST_LOGIN";

        when(userRepository.findByLogin(userLogin)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.loadUserByUsername(userLogin))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid user details");
    }
}