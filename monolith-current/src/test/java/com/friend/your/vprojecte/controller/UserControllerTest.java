package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.test_util.TestPostUtil;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import com.friend.your.vprojecte.test_util.dto.TestUserDtoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlateRepository userPlateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        AppUser setupUser = setupUser();
        userRepository.save(setupUser);
    }


    public AppUser setupUser() {
        AppUser setupUser = TestUserUtil.getTestUser();
        setupUser.setLogin("TEST_LOGIN_UNIQUE");
        setupUser.setEmail("TEST_EMAIL_UNIQUE");
        setupUser.setPassword(passwordEncoder.encode(setupUser.getPassword()));

        return setupUser;
    }

    @Test
    void findAll_GivenValidPageNoAndValidPageSize_ShouldReturnOkAndUserPlatesPage() throws Exception {
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        userPlateRepository.saveAll(testUserPlates);

        mockMvc.perform(get("/users/all?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements", is(5)))
                .andExpect(jsonPath("$.totalElements", is(10)));
    }

    @Test
    void myPage_GivenValidLoggedInUser_ShouldReturnOkAndUserDto() throws Exception {
        AppUser testUser = setupUser();

        mockMvc.perform(get("/users/my_page").with(user(testUser.getLogin())))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.login", is(testUser.getLogin())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.birthdate", is(testUser.getBirthdate().toString())));
    }

    @Test
    void findByLogin_GivenValidUserLogin_ShouldReturnOkAndUserDto() throws Exception {
        AppUser testUser = setupUser();

        MvcResult response = mockMvc.perform(get("/users/TEST_LOGIN_UNIQUE"))
                .andExpect(status().isOk())
                .andReturn();

        String responseData = response.getResponse().getContentAsString();
        AppUserDto returnedDto = objectMapper.readValue(responseData, AppUserDto.class);

        assertEquals(returnedDto.getId(), setupUser().getId());
        assertEquals(returnedDto.getLogin(), setupUser().getLogin());
    }

    @Test
    void findByLoginMatch_GivenValidUserLogin_ShouldReturnOkAndUserPlatesPage() throws Exception {
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        userPlateRepository.save(testUserPlate);

        mockMvc.perform(get("/users/TEST_LOGIN/match?pageNo=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @WithMockUser(username = "TEST_LOGIN_UNIQUE", password = "TEST_PASSWORD")
    void updateUser_GivenValidUserDto_ShouldReturnOkAndUserDto() throws Exception {
        AppUserDto testUserDto = TestUserDtoUtil.getTestUserDtoNoId();

        testUserDto.setPassword(null);

        MvcResult response = mockMvc.perform(put("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseData = response.getResponse().getContentAsString();
        AppUserDto returnedDto = objectMapper.readValue(responseData, AppUserDto.class);

        assertEquals(returnedDto.getId(), 1);
    }

    @Test
    void deleteUser_GivenValidUserId_ShouldReturnOk() throws Exception {
        userPlateRepository.save(TestUserPlateUtil.getTestUserPlate());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        assertFalse(userRepository.existsByLogin("TEST_LOGIN_UNIQUE"));
    }

    @Test
    void userFriendList_GivenLoggedInUser_ShouldReturnOkAndAppUserPlatesPage() throws Exception {
        AppUser testUser = setupUser();
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        testUserPlates.get(0).setUserId(11);
        testUser.getFriendList().addAll(testUserPlates);

        userRepository.save(testUser);

        mockMvc.perform(get("/users/TEST_LOGIN_UNIQUE/friend_list?pageNo=0&pageSize=5").with(user(testUser.getLogin())))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements", is(5)))
                .andExpect(jsonPath("$.totalElements", is(10)));

        assertTrue(userPlateRepository.existsById(testUserPlates.get(0).getUserId()));
    }

    @Test
    void findFriend_GivenLoggedInUserAndValidFriendLogin_ShouldReturnOkAndAppUserPlate() throws Exception {
        AppUser testUser = setupUser();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        testUser.getFriendList().add(testUserPlate);
        userRepository.save(testUser);

        MvcResult response = mockMvc.perform(get("/users/friends/TEST_LOGIN").with(user(testUser.getLogin())))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andReturn();

        String responseData = response.getResponse().getContentAsString();
        AppUserPlate returnedPlate = objectMapper.readValue(responseData, AppUserPlate.class);

        assertEquals(testUserPlate, returnedPlate);
    }

    @Test
    void findFriendsMatch_GivenLoggedInUserAndValidFriendLogin_ShouldReturnAppUserPlatesPage() throws Exception {
        AppUser testUser = setupUser();
        List<AppUser> testUsers = TestUserUtil.getTestUsersMultipleNoId(10);
        List<AppUserPlate> testUserPlates = TestUserPlateUtil.getTestUserPlatesMultiple(10);

        testUserPlates.get(0).setUserId(11);
        testUserPlates.get(0).setUserLogin("NONE1");
        testUserPlates.get(1).setUserLogin("SDF2NONE2");
        testUserPlates.get(2).setUserLogin("532ADNONE3");

        testUser.getFriendList().addAll(testUserPlates);
        userRepository.saveAll(testUsers);
        userRepository.save(testUser);


        mockMvc.perform(get("/users/friends/NONE/match").with(user(testUser.getLogin())))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.pageable.pageSize", is(10)))
                .andExpect(jsonPath("$.numberOfElements", is(3)));
    }

    @Test
    void addFriend_GivenLoggedInUserAndValidUserPlate_ShouldReturnCreated() throws Exception {
        AppUser testUser = setupUser();
        AppUser testUser2 = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate2 = TestUserPlateUtil.getTestUserPlate();

        testUserPlate2.setUserId(2);
        userRepository.save(testUser2);
        userPlateRepository.save(testUserPlate2);

        mockMvc.perform(post("/users/friends")
                        .with(user(testUser.getLogin()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserPlate2)))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string("Friend has been added"));

        Page<AppUserPlate> testFriendList = userRepository.getFriendList(testUser.getLogin(), PageRequest.of(0, 10));

        assertEquals(testFriendList.getContent().size(), 1);
    }

    @Test
    void deleteFriend_GivenLoggedInUserAndValidFriendLogin_ShouldReturnOk() throws Exception {
        AppUser testUser = setupUser();
        AppUser testUser2 = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate2 = TestUserPlateUtil.getTestUserPlate();

        testUserPlate2.setUserId(2);
        testUser.getFriendList().add(testUserPlate2);
        userRepository.save(testUser2);
        userRepository.save(testUser);

        mockMvc.perform(delete("/users/friends/2").with(user(testUser.getLogin())))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend has been deleted"));

        Page<AppUserPlate> testFriendList = userRepository.getFriendList(testUser.getLogin(), PageRequest.of(0, 10));

        assertEquals(testFriendList.getContent().size(), 0);
    }

    @Test
    void getUserWall_GivenValidUserId_ShouldReturnPostDtosPage() throws Exception {
        AppUser testUser = setupUser();
        List<Post> testPosts = TestPostUtil.getTestPostMultipleNoId(10);

        testUser.getPosts().addAll(testPosts);
        userRepository.save(testUser);

        mockMvc.perform(get("/users/1/posts?pageNp=0&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(10)))
                .andExpect(jsonPath("$.numberOfElements", is(5)));
    }

    @Test
    void addPostToUser_GivenValidUserIdAndValidPost_ShouldReturnOkAndPostDto() throws Exception {
        AppUser testUser = setupUser();
        Post testPost = TestPostUtil.getTestPostNoId();

        MvcResult response = mockMvc.perform(post("/users/1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPost)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseData = response.getResponse().getContentAsString();
        PostDto returnedPostDto = objectMapper.readValue(responseData, PostDto.class);
        Page<PostDto> testUserPosts = userRepository.getUserPosts(1, PageRequest.of(0, 10));

        assertEquals(testUserPosts.getContent().get(0).getId(), returnedPostDto.getId());
        assertEquals(testUserPosts.getContent().get(0).getUserLogin(), returnedPostDto.getUserLogin());
    }
}