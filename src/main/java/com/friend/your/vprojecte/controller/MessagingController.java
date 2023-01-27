package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.ChatDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.service.MessagingService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messaging")
public class MessagingController {

    private final MessagingService messagingService;
    private final UserService userService;


    @GetMapping("/chats/{userId}")
    public ResponseEntity<Page<ChatDto>> getUserChats(
            @PathVariable Integer userId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize

    ) {

        return new ResponseEntity<>(messagingService.getUserChats(pageNo, pageSize, userId), HttpStatus.OK);
    }

    @PostMapping("/chats/{receiverId}")
    public ResponseEntity<ChatDto> createNewChat(@PathVariable Integer receiverId) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUserPlate senderPlate = userService.findUserPlate(userLogin);

        return new ResponseEntity<>(messagingService.createChat(senderPlate, receiverId), HttpStatus.CREATED);
    }

    @GetMapping("/{idOfChat}/message_log")
    public ResponseEntity<Page<Message>> getChatMessageLog(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @PathVariable Integer idOfChat
    ) {

        return new ResponseEntity<>(messagingService.getChatMessageLog(pageNo, pageSize, idOfChat), HttpStatus.OK);
    }


    @PostMapping("/{idOfChat}/message_log")
    public ResponseEntity<Message> sendMessage(@PathVariable Integer idOfChat, @RequestBody Message newMessage) {
        return new ResponseEntity<>(messagingService.sendMessage(idOfChat, newMessage), HttpStatus.CREATED);
    }

    @DeleteMapping("/{idOfChat}")
    public ResponseEntity<String> deleteChat(@PathVariable Integer idOfChat) {
        messagingService.deleteChat(idOfChat);

        return new ResponseEntity<>("Chat successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/chats/messages/{idOfMessage}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer idOfMessage) {
        messagingService.deleteMessage(idOfMessage);

        return new ResponseEntity<>("Message successfully deleted", HttpStatus.OK);
    }

}
