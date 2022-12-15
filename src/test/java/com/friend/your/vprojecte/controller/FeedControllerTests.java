package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.PostService;
import com.friend.your.vprojecte.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = FeedController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FeedControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;
    @MockBean
    private UserService userService;
    private AppUser mockUser;
    private RequestPostProcessor mockPostProcessor;

    @BeforeEach
    public void init() {
        mockUser = new AppUser(
                "AppUser",
                "1234",
                "Test",
                "something@proba.test",
                LocalDate.of(1111, 11, 11));
        mockUser.setId(1);

        mockPostProcessor = new CustomRequestPostProcessor(mockUser);
    }

    @Test
    public void FeedController_NewsFeed_ReturnPageOfPosts() throws Exception {

        Post mockPost =
                new Post(1, "Test", "TestUrl", LocalDate.of(1111, 11, 11));
        Page<Post> mockPosts = new PageImpl<>(Arrays.asList(mockPost), PageRequest.of(0, 10), 1);

        when(postService.findAll(1, 10)).thenReturn(mockPosts);

        ResultActions response = mockMvc.perform(get("/feed/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockPosts.getTotalElements())));
    }

    @Test
    public void FeedController_MakePost_ReturnString() throws Exception {
        Post mockPost =
                new Post(1, "Test", "TestUrl", LocalDate.of(1111, 11, 11));

        when(postService.makePost(Mockito.any(Post.class))).thenReturn(mockPost);

        ResultActions response = mockMvc.perform(post("/feed/make")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockPost)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void FeedController_Like_ReturnString() throws Exception {

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        doNothing().when(postService).like(1, 1);

        ResultActions response = mockMvc.perform(post("/feed/like/1")
                .with(mockPostProcessor));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void FeedController_RemoveLikeFromPost_ReturnString() throws Exception {

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        doNothing().when(postService).removeLike(1, 1);

        ResultActions response = mockMvc.perform(put("/feed/like/1")
                .with(mockPostProcessor));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void FeedController_ShowComments_ReturnPageOfComments() throws Exception {
        Comment mockComment = new Comment("Test", 1);
        Page<Comment> mockComments =
                new PageImpl<>(Arrays.asList(mockComment), PageRequest.of(0, 10), 1);

        when(postService.showComments(1, 10, 1)).thenReturn(mockComments);

        ResultActions response = mockMvc.perform(get("/feed/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockComments.getTotalElements())));
    }

    @Test
    public void FeedController_CommentOnPost_ReturnString() throws Exception {
        String mockMessage = "Test message";

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        doNothing().when(postService).comment(1, 1, mockMessage);

        ResultActions response = mockMvc.perform(post("/feed/comment/1")
                .with(mockPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockMessage)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }



}
