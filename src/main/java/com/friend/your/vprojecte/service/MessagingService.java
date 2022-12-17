package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import org.springframework.data.domain.Page;

public interface MessagingService {

    Chat createChat(int senderId, int receiverId);

    Chat findChat(String chatId);

    Page<Message> getChatMessageLog(int pageNo, int pageSize, String idOfChat);

    Message sendMessage(String idOfChat, Message newMessage);

    void deleteChat(String idOfChat);

    void deleteMessage(String idOfChat, int idOfMessage);
}
