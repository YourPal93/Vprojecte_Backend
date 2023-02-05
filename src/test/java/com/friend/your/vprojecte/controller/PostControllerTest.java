package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

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
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlateRepository userPlateRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void setup() {
        AppUser setupUser = TestUserUtil.getTestUserNoId();
        AppUserPlate setupUserPlate = TestUserPlateUtil.getTestUserPlate();

        setupUser.setLogin("TEST_LOGIN_UNIQUE");
        setupUser.setEmail("TEST_EMAIL_UNIQUE");
        setupUser.setPassword(passwordEncoder.encode(setupUser.getPassword()));
        setupUserPlate.setUserLogin("TEST_LOGIN_UNIQUE");

        Post setupPost = TestPostUtil.getTestPostNoId();
        Comment setupComment = new Comment();
        Like setupLike = new Like();

        setupComment.setMessage("TEST_COMMENT");
        setupComment.setPostId(1);
        setupComment.setCreatedBy(setupUserPlate);
        setupComment.setCreationDate(LocalDateTime.now());

        setupLike.setPostId(1);
        setupLike.setUserId(1);

        setupPost.setUserLogin("TEST_LOGIN_UNIQUE");
        setupPost.getComments().add(setupComment);
        setupPost.getLikes().add(setupLike);

        userRepository.save(setupUser);
        userPlateRepository.save(setupUserPlate);
        postRepository.save(setupPost);
    }

    @Test
    @Order(1)
    void newsFeed_GivenValidPageParams_ShouldReturnOkAndPostDtosPage() throws Exception {

        mockMvc.perform(get("/posts/?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.pageable.pageSize", is(5)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].userLogin", is("TEST_LOGIN_UNIQUE")))
                .andExpect(jsonPath("$.content[0].description", is("TEST_DESCRIPTION")));

    }

    @Test
    @Order(2)
    void likePost_GivenValidPostIdAndLoggedInUser_ShouldReturnCreated() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        testUserPlate.setUserId(2);
        userPlateRepository.save(testUserPlate);
        userRepository.save(testUser);

        mockMvc.perform(post("/posts/1/likes").with(user("TEST_LOGIN")))
                .andExpect(authenticated())
                .andExpect(status().isCreated())
                .andExpect(content().string("Liked"));

        assertTrue(likeRepository.existsById(2));
    }

    @Test
    @Order(3)
    void removeLikeFromPost() throws Exception {

        mockMvc.perform(delete("/posts/1/likes").with(user("TEST_LOGIN")))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string("Like removed"));

        assertFalse(likeRepository.existsById(2));
    }

    @Test
    @Order(4)
    void getComments_GivenValidPostId_ShouldReturnOkAndPostCommentDtosPage() throws Exception {

        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].userLogin", is("TEST_LOGIN_UNIQUE")));

    }

    @Test
    @Order(5)
    void commentOnPost_GivenLoggedInUserAndValidComment_ShouldReturnCreatedAndCommentDto() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();
        CommentDto testCommentDto = new CommentDto();

        testUser.setLogin("TEST_LOGIN3");
        testUser.setEmail("TEST_EMAIL3");
        testUserPlate.setUserId(3);
        testUserPlate.setUserLogin("TEST_LOGIN3");

        testCommentDto.setMessage("TEST_MESSAGE2");
        testCommentDto.setUserLogin(testUserPlate.getUserLogin());
        testCommentDto.setCreationDate(LocalDateTime.now());

        userRepository.save(testUser);
        userPlateRepository.save(testUserPlate);

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.userLogin", is(testCommentDto.getUserLogin())))
                .andExpect(jsonPath("$.message", is(testCommentDto.getMessage())));

        assertTrue(commentRepository.existsById(2));
    }

    @Test
    @Order(6)
    void createPost_GivenVaidPostDto_ShouldReturnCreatedAndPostDto() throws Exception {
        PostDto testPostDto = new PostDto();

        testPostDto.setUrl("NONE");
        testPostDto.setUserLogin("TEST_LOGIN_UNIQUE");
        testPostDto.setDescription("TEST_DESCRIPTION_UNIQUE");
        testPostDto.setCreationDate(LocalDateTime.now());

        mockMvc.perform(post("/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPostDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.url", is(testPostDto.getUrl())))
                .andExpect(jsonPath("$.userLogin", is(testPostDto.getUserLogin())))
                .andExpect(jsonPath("$.description", is(testPostDto.getDescription())));

        assertTrue(postRepository.existsById(2));
    }
}