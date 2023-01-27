package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.ChatDto;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import org.springframework.data.domain.Page;

public interface MessagingService {

    Page<ChatDto> getUserChats(int pageNo, int pageSize, Integer userId);

    ChatDto createChat(AppUserPlate senderPlate, Integer receiverId);

    ChatDto findChat(Integer chatId);

    Page<Message> getChatMessageLog(int pageNo, int pageSize, Integer idOfChat);

    Message sendMessage(Integer idOfChat, Message newMessage);

    void deleteChat(Integer idOfChat);

    void deleteMessage(Integer idOfMessage);
}
