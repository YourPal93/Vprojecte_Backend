package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.ChatRepository;
import com.friend.your.vprojecte.dao.MessageRepository;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.ChatDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.test_util.TestChatUtil;
import com.friend.your.vprojecte.test_util.TestUserPlateUtil;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MessagingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlateRepository userPlateRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        AppUser setupUser1 = TestUserUtil.getTestUserNoId();
        AppUser setupUser2 = TestUserUtil.getTestUserNoId();


        AppUserPlate setupUserPlate1 = TestUserPlateUtil.getTestUserPlate();
        AppUserPlate setupUserPlate2 = TestUserPlateUtil.getTestUserPlate();

        setupUser1.setLogin("TEST_LOGIN_UNIQUE1");
        setupUser1.setEmail("TEST_EMAIL_UNIQUE1");
        setupUser1.setPassword(passwordEncoder.encode(setupUser1.getPassword()));

        setupUser2.setLogin("TEST_LOGIN_UNIQUE2");
        setupUser2.setEmail("TEST_EMAIL_UNIQUE2");
        setupUser2.setPassword(passwordEncoder.encode(setupUser2.getPassword()));

        setupUserPlate1.setUserLogin(setupUser1.getLogin());
        setupUserPlate2.setUserId(2);
        setupUserPlate2.setUserLogin(setupUser2.getLogin());

        userPlateRepository.save(setupUserPlate1);
        userPlateRepository.save(setupUserPlate2);

        Chat setupChat = TestChatUtil.getTestChatNoId();
        setupChat.setMessageLog(new HashSet<>());
        setupChat.getMessageLog().add(new Message(2, "TEST_MESSAGE"));
        setupChat.getUsers().add(setupUserPlate1);
        setupChat.getUsers().add(setupUserPlate2);
        chatRepository.save(setupChat);

        setupUser1.getChatLog().add(setupChat);
        setupUser2.getChatLog().add(setupChat);


        userRepository.save(setupUser1);
        userRepository.save(setupUser2);

    }

    @Test
    void getUserChats_GivenValidUserId_ShouldReturnOkAndChatDtosPage() throws Exception {

        mockMvc.perform(get("/chats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].receiverPlate.userId", is(2)))
                .andExpect(jsonPath("$.content[0].receiverPlate.userLogin", is("TEST_LOGIN_UNIQUE2")))
                .andDo(print());
    }

    @Test
    void createNewChat_GivenLoggedInUserAndValidReceiverUserLogin_ShouldReturnCreatedAndChatDto() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserPlate testUserPlate = TestUserPlateUtil.getTestUserPlate();

        testUserPlate.setUserId(3);
        userRepository.save(testUser);
        userPlateRepository.save(testUserPlate);

        MvcResult response = mockMvc.perform(post("/chats/TEST_LOGIN").with(user("TEST_LOGIN_UNIQUE1")))
                .andExpect(authenticated())
                .andExpect(status().isCreated())
                .andReturn();

        String responseData = response.getResponse().getContentAsString();
        ChatDto returnedChatDto = objectMapper.readValue(responseData, ChatDto.class);

        assertEquals(returnedChatDto.getId(), 2);
        assertEquals(returnedChatDto.getReceiverPlate().getUserId(), 3);
        assertEquals(returnedChatDto.getReceiverPlate().getUserLogin(), "TEST_LOGIN");
    }

    @Test
    void getChatMessageLog_GivenValidChatId_ShouldReturnOkAndMessagesPage() throws Exception {

        mockMvc.perform(get("/chats/1/message_log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].author", is(2)));
    }

    @Test
    void sendMessage_GivenValidChatId_ShouldReturnCreatedAndAppUserPlate() throws Exception {
        Message testMessage = new Message();

        testMessage.setMessageContent("TEST_MESSAGE1");
        testMessage.setAuthor(1);

        mockMvc.perform(post("/chats/1/message_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMessage)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.userLogin", is("TEST_LOGIN_UNIQUE1")));


    }

    @Test
    void deleteChat_GivenValidChatId_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/chats/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chat successfully deleted"));

        assertFalse(chatRepository.existsById(1));
    }

    @Test
    void deleteMessage_GivenValidMessageId_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/chats/message_log/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message successfully deleted"));

        assertFalse(messageRepository.existsById(1));
    }
}