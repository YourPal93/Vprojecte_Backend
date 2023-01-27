package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.ChatRepository;
import com.friend.your.vprojecte.dao.MessageRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
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
    private final UserPlateJPARepository userPlateRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ChatDto> getUserChats(int pageNo, int pageSize, Integer userId) {
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
    public ChatDto createChat(AppUserPlate senderPlate, Integer receiverId) {
        // TODO: createChat - add support for group chats

        log.info("Creating new chat between users {} and {}", senderPlate.getUserId(), receiverId);

        AppUserPlate receiverPlate = userPlateRepository.findById(receiverId)
                        .orElseThrow(()-> new RuntimeException("User plate not found"));
        AppUser sender = userRepository.findById(senderPlate.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AppUser receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    public ChatDto findChat(Integer chatId) {
        log.info("Retrieving chat with id {}", chatId);

        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat log not found"));
        ChatDto chatDto = new ChatDto();

        chatDto.setId(chatId);
        chatDto.setReceiverPlate(chat.getUsers().get(0));

        return chatDto;
    }

    @Override
    public Page<Message> getChatMessageLog(int pageNo, int pageSize, Integer idOfChat) {
        log.info("Retrieving message log from chat {}", idOfChat);

        Chat chat = chatRepository.findById(idOfChat).orElseThrow(() -> new RuntimeException("Chat log not found"));
        Page<Message> messageLog = PageUtil.pageFromCollection(pageNo, pageSize, chat.getMessageLog());

        return messageLog;
    }

    @Override
    public Message sendMessage(Integer idOfChat, Message newMessage) {
        log.info("Sending new message in chat {}", idOfChat);

        newMessage.setChatId(idOfChat);
        Message message = messageRepository.save(newMessage);

        return message;
    }

    @Override
    public void deleteChat(Integer idOfChat) {
        log.info("Deleting chat {}", idOfChat);

        chatRepository.deleteById(idOfChat);
    }

    @Override
    public void deleteMessage(Integer idOfMessage) {
        log.info("Deleting message with id {}", idOfMessage);

        messageRepository.deleteById(idOfMessage);
    }
}
