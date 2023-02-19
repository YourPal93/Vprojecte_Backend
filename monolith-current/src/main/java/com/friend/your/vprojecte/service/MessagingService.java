package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.ChatDto;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import org.springframework.data.domain.Page;

public interface MessagingService {

    Page<ChatDto> getUserChatLog(int pageNo, int pageSize, Integer userId);

    ChatDto createChat(String userLogin, String receiverLogin);

    Page<Message> getChatMessageLog(int pageNo, int pageSize, Integer chatId);

    AppUserPlate sendMessage(Integer chatId, Message newMessage);

    void deleteChat(Integer chatId);

    void deleteMessage(Integer messageId);
}
