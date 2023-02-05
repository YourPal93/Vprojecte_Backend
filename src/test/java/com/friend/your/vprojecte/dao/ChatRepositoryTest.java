package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.test_util.TestChatUtil;
import com.friend.your.vprojecte.test_util.TestMessageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-h2.properties")
class ChatRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository underTest;

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();;
        underTest.deleteAll();
    }

    @Test
    void getMessageLog_GivenValidChatId_ShouldReturnPageOfMessages() {
        Chat testChat = TestChatUtil.getTestChat();
        List<Message> testMessages = TestMessageUtil.getTestMessageMultiple(10);

        underTest.save(testChat);
        messageRepository.saveAll(testMessages);

        Page<Message> messageLogPage = underTest.getMessageLog(testChat.getId(), PageRequest.of(0, 10));

        assertEquals(messageLogPage.getTotalElements(), 10);
    }

}