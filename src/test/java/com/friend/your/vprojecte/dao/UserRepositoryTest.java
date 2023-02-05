package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.ChatDto;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.test_util.TestGroupUtil;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-h2.properties")
class UserRepositoryTest {
    @Autowired
    private UserPlateRepository userPlateRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        userPlateRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    void getUserFriendList_GivenValidUserLogin_ShouldReturnPageOfAppUsePlates() {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        testUser.getFriendList().addAll(testUserPlates);
        underTest.save(testUser);

        Page<AppUserPlate> testUserFriendListPage =
                underTest.getFriendList(testUser.getLogin(), PageRequest.of(0 , 10));

        assertEquals(testUserFriendListPage.getContent(), testUserPlates);
    }

    @Test
    void findFriendsMatching_GivenValidUserLoginAndValidFriendLogin_ShouldReturnPageOfMatchingAppUserPlates() {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        testUserPlates.get(1).setUserLogin("NONE");
        testUser.getFriendList().addAll(testUserPlates);

        underTest.save(testUser);

        Page<AppUserPlate> testUserFriendList =
                underTest.findFriendsMatching("TEST_LOGIN", "TEST_LOGIN", PageRequest.of(0, 10));
        AppUserPlate testUniquePlate = testUserPlates.get(1);

        assertFalse(testUserFriendList.getContent().contains(testUniquePlate));
        assertEquals(testUserFriendList.getContent().size(), 9);
    }

    @Test
    void findFriend_GivenValidUserLoginAndValidFriendLogin_ShouldReturnAppUserPlate() {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        testUserPlates.get(1).setUserLogin("NONE");
        testUser.getFriendList().addAll(testUserPlates);

        underTest.save(testUser);

        AppUserPlate testUserFriend = underTest.findFriend("TEST_LOGIN", "NONE")
                .orElseThrow(() -> new RuntimeException("User plate not found"));


        assertEquals(testUserPlates.get(1), testUserFriend);
    }

    @Test
    void findUserGroups_GivenValidUserLogin_ShouldReturnPageOfGroupDtos() {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        Group testGroup = TestGroupUtil.getTestGroupNoId();

        testUser.getGroups().add(testGroup);
        underTest.save(testUser);
        groupRepository.save(testGroup);

        Page<GroupDto> savedUserChatDtosPage = underTest.findUserGroups(testUser.getLogin(), PageRequest.of(0, 10));

        assertEquals(savedUserChatDtosPage.getTotalElements(), 1);
    }

    @Test
    void getUserPosts_GivenValidUserLogin_ShouldReturnPageOfPostDtos() {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        Post testPost = new Post();

        testPost.setUserLogin(testUser.getLogin());
        testUser.getPosts().add(testPost);

        underTest.save(testUser);

        Page<PostDto> savedPosts = underTest.getUserPosts(testUser.getId(), PageRequest.of(0, 10));

        assertEquals(savedPosts.getContent().get(0).getId(), testPost.getId());
    }
}