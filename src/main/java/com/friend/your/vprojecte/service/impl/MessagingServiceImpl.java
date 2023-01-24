package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.ChatRepository;
import com.friend.your.vprojecte.dao.MessageRepository;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.service.MessagingService;
import com.friend.your.vprojecte.utility.IdMakerUtil;
import com.friend.your.vprojecte.utility.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MessagingServiceImpl implements MessagingService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public Chat createChat(int senderId, int receiverId) {
        log.info("Creating new chat between users {} and {}", senderId, receiverId);

        return chatRepository.save(new Chat(IdMakerUtil.twoInts(senderId, receiverId)));
    }

    @Override
    public Chat findChat(String chatId) {
        log.info("Retrieving chat with id {}", chatId);

        return chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat log not found"));
    }

    @Override
    public Page<Message> getChatMessageLog(int pageNo, int pageSize, String idOfChat) {
        log.info("Retrieving message log from chat {}", idOfChat);

        Chat chat = chatRepository.findById(idOfChat).orElseThrow(() -> new RuntimeException("Chat log not found"));
        Page<Message> messageLog = PageUtil.pageFromList(pageNo, pageSize, chat.getMessageLog());

        return messageLog;
    }

    @Override
    public Message sendMessage(String idOfChat, Message newMessage) {
        log.info("Sending new message in chat {}", idOfChat);

        Chat chat = chatRepository.findById(idOfChat).orElseThrow(() -> new RuntimeException("Chat lof not found"));
        chat.getMessageLog().add(newMessage);
        chatRepository.save(chat);

        return newMessage;
    }

    @Override
    public void deleteChat(String idOfChat) {
        log.info("Deleting chat {}", idOfChat);

        Chat chat = chatRepository.findById(idOfChat).orElseThrow(() -> new RuntimeException("Chat lof not found"));
        chatRepository.delete(chat);
    }

    @Override
    public void deleteMessage(String idOfChat, int idOfMessage) {
        log.info("Deleting message {} from chat {}", idOfMessage, idOfChat);

        Message message = messageRepository.findById(idOfMessage)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        Chat chat = chatRepository.findById(idOfChat).orElseThrow(() -> new RuntimeException("Chat lof not found"));
        chat.getMessageLog().remove(message);
        chatRepository.save(chat);
    }
}
