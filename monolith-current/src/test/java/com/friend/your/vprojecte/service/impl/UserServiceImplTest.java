package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import com.friend.your.vprojecte.test_util.dto.TestUserDtoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPlateRepository userPlateRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private UserServiceImpl underTest;

    @Test
    void findAllUsers_GivenPageNoAndPageSize_ShouldReturnPageOfAppUserPlates() {
        Page<AppUserPlate> testUserPlatesPage = mock(Page.class);

        when(userPlateRepository.findAll(any(Pageable.class))).thenReturn(testUserPlatesPage);

        Page<AppUserPlate> returnedPage = underTest.findAllUsers(0, 10);

        assertEquals(testUserPlatesPage, returnedPage);
    }

    @Test
    void findUserPlate_GivenValidLogin_ShouldReturnAppUserPlate() {
        AppUserPlate testUserPlate = mock(AppUserPlate.class);

        when(userPlateRepository.findByUserLogin(anyString())).thenReturn(Optional.of(testUserPlate));

        AppUserPlate returnedPlate = underTest.findUserPlate("TEST_LOGIN");

        assertEquals(testUserPlate, returnedPlate);
    }

    @Test
    void findUser_GivenValidLogin_ShouldReturnUserDto() {
        AppUser testUser = TestUserUtil.getTestUser();

        when(userRepository.findByLogin("TEST_USER")).thenReturn(Optional.of(testUser));

        AppUserDto returnedDto = underTest.findUser("TEST_USER");

        assertEquals(returnedDto.getId(), testUser.getId());
    }

    @Test
    void findUser_GivenValidId_ShouldReturnUserDto() {
        AppUser testUser = TestUserUtil.getTestUser();

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        AppUserDto returnedDto = underTest.findUser(1);

        assertEquals(returnedDto.getId(), testUser.getId());
    }

    @Test
    void findByLoginMatch_GivenValidLogin_ShouldReturnPageOfAppUserPlates() {
        Page<AppUserPlate> testAppUserPlatesPage = mock(Page.class);

        when(userPlateRepository.findByUserLoginContaining(anyString(), any(Pageable.class)))
                .thenReturn(testAppUserPlatesPage);

        Page<AppUserPlate> returnedPage = underTest.findByLoginMatch(0, 10,"TEST_LOGIN");

        assertEquals(testAppUserPlatesPage, returnedPage);
    }

    @Test
    void save_GivenValidUserDto_ShouldReturnUserDto() {
        AppUserDto testUserDto = TestUserDtoUtil.getTestUserDtoNoId();
        AppUser testUser = TestUserUtil.getTestUser();

        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);
        when(userPlateRepository.save(any(AppUserPlate.class))).thenReturn(mock(AppUserPlate.class));

        AppUserDto returnedDto = underTest.saveUser(testUserDto);

        verify(userPlateRepository).save(any(AppUserPlate.class));
        assertEquals(returnedDto.getId(), testUser.getId());
    }

    @Test
    void update_GivenValidUserDtoWithoutPasswordAndValidUserLogin_ShouldReturnUserDto() {
        String testLogin = "TEST_LOGIN";
        AppUserDto testUserDto = TestUserDtoUtil.getTestUserDto();
        AppUser testUser = TestUserUtil.getTestUser();

        testUserDto.setPassword(null);

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        AppUserDto returnedDto = underTest.updateUser(testLogin, testUserDto);

        assertEquals(returnedDto.getId(), testUserDto.getId());
    }

    @Test
    void delete_GivenValidId_ShouldNotReturnAnything() {
        Integer testId = 1;

        doNothing().when(userRepository).deleteById(anyInt());
        doNothing().when(userPlateRepository).deleteById(anyInt());

        underTest.deleteUser(testId);

        verify(userRepository).deleteById(anyInt());
        verify(userPlateRepository).deleteById(anyInt());
    }

    @Test
    void getUserWall_GivenValidUserLogin_ShouldReturnPageOfPostDtos() {
        Page<PostDto> testPostDtosPage = mock(Page.class);

        when(userRepository.getUserPosts(anyInt(), any(Pageable.class))).thenReturn(testPostDtosPage);

        Page<PostDto> returnedPage = underTest.getUserWall(0, 10, 1);

    }

    @Test
    void addPostToUser_GivenValidUserIdAndValidPost_ShouldReturnPostDto() {
        Post testPost = mock(Post.class);
        AppUser testUser = mock(AppUser.class);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(postRepository.save(testPost)).thenReturn(testPost);
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        underTest.addPostToUser(testPost, 1);

        verify(userRepository).findById(anyInt());
        verify(userRepository).save(testUser);
    }
}