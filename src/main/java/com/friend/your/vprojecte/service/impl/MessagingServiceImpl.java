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
import com.friend.your.vprojecte.service.MessagingService;
import com.friend.your.vprojecte.utility.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MessagingServiceImpl implements MessagingService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserPlateRepository userPlateRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ChatDto> getUserChatLog(int pageNo, int pageSize, Integer userId) {
        log.info("Requesting chats of user with id {} page {} page size {}", userId, pageNo, pageSize);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Chat> chats = user.getChatLog();
        List<ChatDto> chatDtos = new ArrayList<>();

        int start = pageNo * pageSize;
        int end = start + pageSize;
        if(end > chats.size()) {
            end = chats.size();
        }

        for(Chat chat : chats.subList(start, end)) {
            List<AppUserPlate> chatUsers = chat.getUsers();
            AppUserPlate receiverPlate =
                    chatUsers.get(0).getUserId().equals(userId) ? chatUsers.get(1) : chatUsers.get(0);

            ChatDto chatDto = new ChatDto();
            chatDto.setId(chat.getId());
            chatDto.setReceiverPlate(receiverPlate);
            chatDtos.add(chatDto);
        }

        Page<ChatDto> userChats = PageUtil.listToPage(pageNo, pageSize, chatDtos, chats.size());

        return userChats;
    }

    @Override
    public ChatDto createChat(String userLogin, String receiverLogin) {
        // TODO: createChat - add support for group chats

        log.info("Creating new chat between users {} and {}", userLogin, receiverLogin);

        AppUser sender = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUser receiver = userRepository.findByLogin(receiverLogin)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUserPlate senderPlate = new AppUserPlate(sender.getId(), userLogin);
        AppUserPlate receiverPlate = new AppUserPlate(receiver.getId(), receiverLogin);

        Chat newChat = new Chat();

        newChat.setUsers(new ArrayList<>());
        newChat.getUsers().add(senderPlate);
        newChat.getUsers().add(receiverPlate);


        Chat savedChat = chatRepository.save(newChat);

        sender.getChatLog().add(savedChat);
        receiver.getChatLog().add(savedChat);

        userRepository.save(sender);
        userRepository.save(receiver);

        ChatDto chatDto = new ChatDto();

        chatDto.setId(savedChat.getId());
        chatDto.setReceiverPlate(receiverPlate);

        return chatDto;
    }

    @Override
    public Page<Message> getChatMessageLog(int pageNo, int pageSize, Integer chatId) {
        log.info("Retrieving message log from chat {}", chatId);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return chatRepository.getMessageLog(chatId, pageable);
    }

    @Override
    public AppUserPlate sendMessage(Integer chatId, Message newMessage) {
        log.info("Sending new message in chat {}", chatId);

        newMessage.setChatId(chatId);
        messageRepository.save(newMessage);

        AppUserPlate authorPlate = userPlateRepository.findById(newMessage.getAuthor())
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        return authorPlate;
    }

    @Override
    public void deleteChat(Integer chatId) {
        log.info("Deleting chat {}", chatId);

        chatRepository.deleteById(chatId);
    }

    @Override
    public void deleteMessage(Integer messageId) {
        log.info("Deleting message with id {}", messageId);

        messageRepository.deleteById(messageId);
    }
}
