package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.enums.AddRequestTypes;
import com.friend.your.vprojecte.test_util.TestGroupUtil;
import com.friend.your.vprojecte.test_util.TestPostUtil;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GroupAdministrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlateRepository userPlateRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddRequestRepository addRequestRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setup() {
        AppUser setupUser = TestUserUtil.getTestUserNoId();
        AppUserPlate setupUserPlate = TestUserPlateUtil.getTestUserPlate();
        Role setupRole = new Role();

        setupRole.setName("ROLE_GROUP_1_ADMIN");
        setupRole.setRoleType(1);

        setupUser.setLogin("TEST_LOGIN_UNIQUE");
        setupUser.setEmail("TEST_EMAIL_UNIQUE");
        setupUser.setPassword(passwordEncoder.encode(setupUser.getPassword()));
        setupUser.getRoles().add(setupRole);
        setupUserPlate.setUserLogin("TEST_LOGIN_UNIQUE");


        Group setupGroup = TestGroupUtil.getTestGroupNoId();

        setupGroup.getMembers().add(setupUserPlate);

        groupRepository.save(setupGroup);

        setupUser.getGroups().add(setupGroup);

        userRepository.save(setupUser);
        userPlateRepository.save(setupUserPlate);
    }

    @Test
    @Order(1)
    void createGroup_GivenLoggedInUserAndGroup_ShouldReturnCreatedAndGroup() throws Exception {
        Group testGroup = new Group();

        testGroup.setName("TEST_GROUP1");

        mockMvc.perform(post("/groups/administration/").with(user("TEST_LOGIN_UNIQUE"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("TEST_GROUP1")))
                .andExpect(jsonPath("$.members[0].userLogin", is("TEST_LOGIN_UNIQUE")));

        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_2_ADMIN", 1));
    }

    @Test
    @Order(2)
    void updateGroup_GivenValidGroupDto_ShouldReturnOkAndGroupDto() throws Exception {
        GroupDto testGroupDto = new GroupDto();

        testGroupDto.setId(2);
        testGroupDto.setName("TEST_GROUP1_UPDATED");
        testGroupDto.setClosed(true);

        mockMvc.perform(put("/groups/administration/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGroupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name",is(testGroupDto.getName())))
                .andExpect(jsonPath("$.closed", is(true)));

        assertTrue(groupRepository.existsById(2));
        assertTrue(groupRepository.existsByName(testGroupDto.getName()));
    }

    @Test
    @Order(3)
    void deleteGroup_GivenValidGroupId_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/groups/administration/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Group was successfully deleted"));

        assertFalse(groupRepository.existsById(2));
        assertFalse(groupRepository.existsByName("TEST_GROUP1_UPDATED"));
        assertFalse(roleRepository.existsByName("ROLE_GROUP_2_ADMIN"));
        assertFalse(roleRepository.existsByName("ROLE_GROUP_2_MEMBER"));
    }

    @Test
    @Order(4)
    void setModerator_GivenValidGroupIdAndUserId_SholdReturnOk() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();
        Role testRole = new Role();

        testRole.setName("ROLE_GROUP_1_MEMBER");
        testRole.setRoleType(1);

        testUser.getRoles().add(testRole);
        testUserPlate.setUserId(2);

        userRepository.save(testUser);
        userPlateRepository.save(testUserPlate);

        mockMvc.perform(put("/groups/administration/1/moderation/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Moderator was successfully added"));

        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MODERATOR", 2));
        assertFalse(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MEMBER", 2));
    }

    @Test
    @Order(5)
    void deleteModerator_GivenValidGroupIdAndUserId_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/groups/administration/1/moderation/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Moderator was deleted successfully"));

        assertFalse(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MODERATOR", 2));
        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MEMBER", 2));
    }

    @Test
    @Order(6)
    void setAdmin_GivenLoggedInUserAndValidGroupIdAndValidUserId_ShouldReturnOk() throws Exception {

        mockMvc.perform(put("/groups/administration/1/2").with(user("TEST_LOGIN_UNIQUE")))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string("Admin was set successfully"));

        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_ADMIN", 2));
        assertFalse(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_ADMIN", 1));
        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MODERATOR", 1));
    }

    @Test
    @Order(7)
    void deleteMember_GivenValidGroupIdAndUserId_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/groups/administration/1/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Member was deleted successfully"));

        assertFalse(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MODERATOR", 1));
    }

    @Test
    @Order(8)
    void approveMembershipRequest_GivenValidAddRequest_ShouldReturnOk() throws Exception {
        AddRequest testAddRequest = new AddRequest();

        testAddRequest.setType(1);
        testAddRequest.setUserId(1);
        testAddRequest.setReceiverId(1);

        addRequestRepository.save(testAddRequest);

        mockMvc.perform(put("/groups/administration/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAddRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Membership request has been approved"));

        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MEMBER", 1));
        assertFalse(addRequestRepository.existsById(testAddRequest.getId()));
    }

    @Test
    @Order(9)
    void denyMembershipRequest_GivenValidAddRequest_ShouldReturnOK() throws Exception {
        AddRequest testAddRequest = new AddRequest();

        testAddRequest.setType(1);
        testAddRequest.setUserId(1);
        testAddRequest.setReceiverId(1);

        addRequestRepository.save(testAddRequest);

        mockMvc.perform(delete("/groups/administration/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAddRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Request has been denied"));

        assertFalse(addRequestRepository.existsById(testAddRequest.getId()));
    }

    @Test
    @Order(10)
    void deleteGroupPost_GivenValidPostId_ShouldReturnOk() throws Exception {
        Post testPost = TestPostUtil.getTestPostNoId();

        postRepository.save(testPost);

        mockMvc.perform(delete("/groups/administration/feed/" + testPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Post has been deleted"));

        assertFalse(postRepository.existsById(testPost.getId()));
    }
}