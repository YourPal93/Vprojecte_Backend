package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.service.FriendService;
import com.friend.your.vprojecte.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private FriendService friendService;
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

        mockPostProcessor = new CustomRequestPostProcessor(mockUser);
    }

    @Test
    public void UserController_FindAll_ReturnPageOfUsers() throws Exception{
        Page<AppUser> mockUsers = new PageImpl<>(Arrays.asList(mockUser), PageRequest.of(0, 10), 1);

        when(userService.findAll(1, 10)).thenReturn(mockUsers);

        ResultActions response = mockMvc.perform(get("/user/all")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockUsers.getTotalElements())));
    }
    @Test
    public void UserController_MyPage_ReturnAppUser() throws Exception {
        String mockLogin = "Test";
        String mockName = "AppUser";

        when(userService.findByLogin("Test")).thenReturn(mockUser);

        ResultActions response = mockMvc.perform(get("/user/my_page")
                .with(mockPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(mockName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(mockLogin)));
    }

    @Test
    public void UserController_FindByLogin_ReturnAppUser() throws Exception {
        String mockLogin = "Test";
        String mockName = "AppUser";

        when(userService.findByLogin("Test")).thenReturn(mockUser);

        ResultActions response = mockMvc.perform(get("/user/find/Test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(mockName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(mockLogin)));
    }

    @Test
    public void UserController_UpdateUser_ReturnAppUser() throws Exception {

        when(userService.save(Mockito.any(AppUser.class))).thenReturn(mockUser);

        ResultActions response = mockMvc.perform(put("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(mockUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(mockUser.getLogin())));
    }

    @Test
    public void UserController_DeleteUser_ReturnString() throws Exception {

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        doNothing().when(userService).delete(1);

        ResultActions response = mockMvc.perform(delete("/user/delete/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_FriendList_ReturnPageOfUsers() throws Exception{
        Page<AppUser> mockFriends = new PageImpl<>(Arrays.asList(mockUser), PageRequest.of(0, 10), 1);

        when(friendService.findAllFriends(
                Mockito.anyInt(), Mockito.anyInt(), Mockito.any(AppUser.class))).thenReturn(mockFriends);
        when(userService.findByLogin("Test")).thenReturn(mockUser);

        ResultActions response = mockMvc.perform(get("/user/friend_list")
                .with(mockPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockFriends.getTotalElements())));
    }

    @Test
    public void UserController_FindByFriend_ReturnAppUser() throws Exception {
        String mockLogin = "Test";
        String mockName = "AppUser";

        when(friendService.findFriend("Test")).thenReturn(mockUser);

        ResultActions response = mockMvc.perform(get("/user/friend/find/Test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(mockName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(mockLogin)));
    }

    @Test
    public void UserController_AddFriend_ReturnString() throws Exception {

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        given(friendService.addFriend(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/user/friend/add/1")
                .with(mockPostProcessor));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void UserController_DeleteFriend_ReturnString() throws Exception {

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        doNothing().when(friendService).deleteFriend(Mockito.any(AppUser.class), Mockito.anyInt());

        ResultActions response = mockMvc.perform(delete("/user/friend/delete/1")
                .with(mockPostProcessor));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserController_ChatLogs_ReturnPageOfChats() throws Exception{
        Chat mockChat = new Chat("1,2");
        Page<Chat> mockChatLogs = new PageImpl<>(Arrays.asList(mockChat), PageRequest.of(0, 10), 1);

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        when(userService.getChatLogs(1, 10, mockUser)).thenReturn(mockChatLogs);

        ResultActions response = mockMvc.perform(get("/user/communication")
                .with(mockPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockChatLogs.getTotalElements())));
    }
}
