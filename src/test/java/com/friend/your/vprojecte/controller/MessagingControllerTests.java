package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.service.MessagingService;
import com.friend.your.vprojecte.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(MessagingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MessagingControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessagingService messagingService;
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
    public void MessagingController_CreateNewChat_ReturnChat() throws Exception {
        Chat mockChat = new Chat("1,2");

        when(userService.findByLogin("Test")).thenReturn(mockUser);
        when(messagingService.createChat(1, 2)).thenReturn(mockChat);

        ResultActions response = mockMvc.perform(post("/communication/chat/2")
                .with(mockPostProcessor));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void MessagingController_GetChatMessageLog_ReturnPageOfMessages() throws Exception {
        List<Message> mockMessageLog = new ArrayList<>();
        mockMessageLog.add(new Message(1, "test message"));
        Page<Message> mockMessages = new PageImpl<>(mockMessageLog, PageRequest.of(1, 10), 1);


        when(messagingService.getChatMessageLog(1, 10, "1,2")).thenReturn(mockMessages);

        ResultActions response = mockMvc.perform(get("/communication/1,2/message_log")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements",
                        CoreMatchers.is((int) mockMessages.getTotalElements())));
    }

    @Test
    public void MessagingController_SendMessage_ReturnMessage() throws Exception {
        Message mockMessage = new Message(1, "test message");

        when(messagingService.sendMessage(anyString(), any(Message.class))).thenReturn(mockMessage);

        ResultActions response = mockMvc.perform(post("/communication/1,2/message_log")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockMessage)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageContent",
                        CoreMatchers.is(mockMessage.getMessageContent())));

    }

    @Test
    public void MessagingController_DeleteChat_ReturnString() throws Exception {

        doNothing().when(messagingService).deleteChat("1,2");

        ResultActions response = mockMvc.perform(delete("/communication/1,2"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void MessagingController_DeleteMessage_ReturnString() throws Exception {

        doNothing().when(messagingService).deleteMessage("1,2", 1);

        ResultActions response = mockMvc.perform(delete("/communication/1,2/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

}
