package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.test_util.TestGroupUtil;
import com.friend.your.vprojecte.test_util.TestPostUtil;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GroupMembershipControllerTest {

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
    void findAll_GivenNoPageData_ShouldReturnOkAndGroupDtosPage() throws Exception {
        List<Group> testGroup = TestGroupUtil.getTestGroupMultipleNoId(10);

        groupRepository.saveAll(testGroup);

        mockMvc.perform(get("/groups/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(11)))
                .andExpect(jsonPath("$.numberOfElements", is(10)));
    }

    @Test
    @Order(2)
    void findGroupMatch_GivenValidGroupName_ShouldReturnMatchingGroupDtosPage() throws Exception {

        mockMvc.perform(get("/groups/TEST_GROUP5/match"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("TEST_GROUP5")));
    }

    @Test
    @Order(3)
    void joinGroup_GivenLoggedInUserAndValidGroupId_ShouldReturnOkAndGroupRole() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        testUserPlate.setUserId(2);
        userRepository.save(testUser);
        userPlateRepository.save(testUserPlate);

        mockMvc.perform(post("/groups/1/members").with(user(testUser.getLogin())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("ROLE_GROUP_1_MEMBER")))
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.roleType", is(1)));

        assertTrue(roleRepository.existsByNameAndUserId("ROLE_GROUP_1_MEMBER", 2));
    }

    @Test
    @Order(4)
    void sendMembershipRequest_GivenValidAddRequest_ShouldReturnOk() throws Exception {

        AddRequest testRequest = new AddRequest();

        testRequest.setUserId(10);
        testRequest.setReceiverId(1);
        testRequest.setType(1);

        mockMvc.perform(post("/groups/requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Membership request has been sent"));

        assertTrue(addRequestRepository.existsById(1));
    }

    @Test
    @Order(5)
    void makeGroupPost_GivenValidPost_ShouldReturnCreatedAndPostDto() throws Exception {
        Post testPost = TestPostUtil.getTestPostNoId();

        mockMvc.perform(post("/groups/1/feed")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userLogin", is("TEST_LOGIN")))
                .andExpect(jsonPath("$.description", is("TEST_DESCRIPTION")))
                .andExpect(jsonPath("$.url", is("TEST_URL")));

        assertTrue(postRepository.existsById(1));
    }
}