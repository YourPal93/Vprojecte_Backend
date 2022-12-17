package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dao.ChatRepository;
import com.friend.your.vprojecte.dao.MessageRepository;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.service.impl.MessagingServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessagingServiceTests {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessagingServiceImpl messagingService;

    @Test
    public void MessagingService_CreateChat_ReturnChat() {

        Chat mockChat = Mockito.mock(Chat.class);

        when(chatRepository.save(Mockito.any(Chat.class))).thenReturn(mockChat);

        Chat savedChat = messagingService.createChat(1, 2);

        Assertions.assertThat(savedChat).isNotNull();
    }

    @Test
    public void MessagingService_FindChat_ReturnChat() {

        Chat mockChat = Mockito.mock(Chat.class);

        when(chatRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(mockChat));

        Chat foundChat = messagingService.findChat("1,2");

        Assertions.assertThat(foundChat).isNotNull();
    }

    @Test
    public void MessagingService_GetChatMessageLog_ReturnPageOfMessages() {

        Chat mockChat = new Chat("1,2");
        List<Message> mockMessageLog = new ArrayList<>();
        mockMessageLog.add(new Message(1, "test"));
        mockMessageLog.add(new Message(1,"test1"));
        mockMessageLog.add(new Message(1,"test2"));
        mockChat.setMessageLog(mockMessageLog);


        when(chatRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(mockChat));

        Page<Message> pageOfMessages = messagingService.getChatMessageLog(1, 10, "1,2");

        Assertions.assertThat(pageOfMessages).isNotNull();
        Assertions.assertThat(pageOfMessages.getContent()).isEqualTo(mockMessageLog);
    }

    @Test
    public void MessagingService_SendMessage_ReturnPageOfMessages() {

        Chat mockChat = new Chat("1,2");
        mockChat.setMessageLog(new ArrayList<>());
        Message mockMessage = new Message(1, "test message");


        when(chatRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(mockChat));
        when(chatRepository.save(Mockito.any(Chat.class))).thenReturn(mockChat);

        Message addedMessage = messagingService.sendMessage("1,2", mockMessage);

        Assertions.assertThat(addedMessage).isNotNull();
        Assertions.assertThat(mockChat.getMessageLog().contains(addedMessage)).isTrue();
    }

    @Test
    public void MessagingService_DeleteMessage_ReturnNothing(){
        Chat mockChat = new Chat("1,2");
        List<Message> mockMessageLog = new ArrayList<>();
        Message mockMessage = new Message(1, "test message");
        mockMessageLog.add(new Message(1, "test"));
        mockMessageLog.add(new Message(1,"test1"));
        mockMessageLog.add(new Message(1,"test2"));
        mockChat.setMessageLog(mockMessageLog);

        when(chatRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(mockChat));
        when(messageRepository.findById(1)).thenReturn(Optional.ofNullable(mockMessage));
        when(chatRepository.save(Mockito.any(Chat.class))).thenReturn(mockChat);

        messagingService.deleteMessage("1,2", 1);

        Assertions.assertThat(mockChat.getMessageLog().contains(mockMessage)).isFalse();

    }
}
