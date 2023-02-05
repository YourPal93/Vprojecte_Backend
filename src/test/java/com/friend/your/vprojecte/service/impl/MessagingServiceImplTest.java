package com.friend.your.vprojecte.service.impl;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagingServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPlateRepository userPlateRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessagingServiceImpl underTest;

    @Test
    void getUserChatLog_GivenValidUserLogin_ShouldReturnPageOfChatDtos() {
        AppUser testUser = TestUserUtil.getTestUser();
        List<Chat> testChats = TestChatUtil.getTestChatMultiple(5);

        testChats.forEach(chat -> { chat.getUsers().addAll(TestUserPlateUtil.getTestUserPlatesMultiple(2)); });
        testUser.getChatLog().addAll(testChats);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

        Page<ChatDto> returnedPage = underTest.getUserChatLog(0, 10, 1);

        assertEquals(returnedPage.getTotalElements(), testChats.size());
    }

    @Test
    void createChat_GivenValidUserLoginAndValidRecieverLogin_ShouldReturnChatDto() {
        Chat testChat = TestChatUtil.getTestChat();
        List<AppUser> testUsers = TestUserUtil.getTestUsersMultiple(2);
        testUsers.get(0).setLogin("TEST_USER_1");
        testUsers.get(1).setLogin("TEST_USER_2");

        when(userRepository.findByLogin("TEST_USER_1")).thenReturn(Optional.of(testUsers.get(0)));
        when(userRepository.findByLogin("TEST_USER_2")).thenReturn(Optional.of(testUsers.get(1)));
        when(userRepository.save(any(AppUser.class))).thenReturn(mock(AppUser.class));
        when(chatRepository.save(any(Chat.class))).thenReturn(testChat);

        ChatDto returnedDto = underTest.createChat("TEST_USER_1", "TEST_USER_2");

        assertEquals(returnedDto.getReceiverPlate().getUserId(), 2);
    }

    @Test
    void getChatMessageLog_GivenValidChatId_ShouldReturnPageOfMessages() {
        Page<Message> testMessagesPage = mock(Page.class);

        when(chatRepository.getMessageLog(anyInt(), any(Pageable.class))).thenReturn(testMessagesPage);

        Page<Message> returnedPage = underTest.getChatMessageLog(0, 10, 1);

        assertEquals(testMessagesPage, returnedPage);
    }

    @Test
    void sendMessage_GivenValidMessage_ShouldReturnMessage() {
        Message testMessage = mock(Message.class);

        when(messageRepository.save(any(Message.class))).thenReturn(mock(Message.class));
        when(userPlateRepository.findById(anyInt())).thenReturn(Optional.of(mock(AppUserPlate.class)));

        underTest.sendMessage(1, testMessage);

        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void deleteChat_GivenValidChatId_ShouldNotReturnAnything() {

        doNothing().when(chatRepository).deleteById(anyInt());

        underTest.deleteChat(1);

        verify(chatRepository).deleteById(anyInt());
    }

    @Test
    void deleteMessage_GivenValidMessageId_ShouldNotReturnAnything() {

        doNothing().when(messageRepository).deleteById(anyInt());

        underTest.deleteMessage(1);

        verify(messageRepository).deleteById(anyInt());
    }
}