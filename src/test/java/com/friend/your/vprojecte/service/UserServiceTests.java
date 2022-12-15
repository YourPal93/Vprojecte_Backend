package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_FindAll_ReturnPageOfAppUsers() {
        Page<AppUser> mockUsers = Mockito.mock(Page.class);

        when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockUsers);

        Page<AppUser> users = userService.findAll(1, 15);

        Assertions.assertThat(users).isNotNull();
    }

    @Test
    public void UserService_FindById_ReturnAppUser() {
        AppUser mockUser = Mockito.mock(AppUser.class);

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(mockUser));

        AppUser user = userService.findById(1);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void UserService_FindByLogin_ReturnAppUser() {
        AppUser mockUser = Mockito.mock(AppUser.class);

        when(userRepository.findByLogin("Test")).thenReturn(Optional.ofNullable(mockUser));

        AppUser user = userService.findByLogin("Test");

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void UserService_Save_ReturnAppUser() {
        AppUser mockUser = new AppUser(
                "AppUser",
                "1234",
                "Test",
                "something@proba.test",
                LocalDate.of(1111, 11, 11));

        when(userRepository.save(Mockito.any(AppUser.class))).thenReturn(mockUser);

        AppUser savedUser = userService.save(mockUser);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getRoles()).isNotNull();
    }

    @Test
    public void UserService_Delete_ReturnVoid() {
        assertAll(() -> userService.delete(1));
    }

}
