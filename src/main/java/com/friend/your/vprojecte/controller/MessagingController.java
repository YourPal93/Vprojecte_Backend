package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import com.friend.your.vprojecte.service.MessagingService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/communication")
public class MessagingController {

    private final MessagingService messagingService;
    private final UserService userService;

    @PostMapping("/chat/{receiverId}")
    public ResponseEntity<Chat> createNewChat(HttpServletRequest request, @PathVariable int receiverId) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        return new ResponseEntity<>(messagingService.createChat(user.getId(), receiverId), HttpStatus.CREATED);
    }

    @GetMapping("/{idOfChat}/message_log")
    public ResponseEntity<Page<Message>> getChatMessageLog(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @PathVariable String idOfChat
    ) {

        return new ResponseEntity<>(messagingService.getChatMessageLog(pageNo, pageSize, idOfChat), HttpStatus.OK);
    }


    @PostMapping("/{idOfChat}/message_log")
    public ResponseEntity<Message> sendMessage(@PathVariable String idOfChat, @RequestBody Message newMessage) {
        return new ResponseEntity<>(messagingService.sendMessage(idOfChat, newMessage), HttpStatus.CREATED);
    }

    @DeleteMapping("/{idOfChat}")
    public ResponseEntity<String> deleteChat(@PathVariable String idOfChat) {
        messagingService.deleteChat(idOfChat);

        return new ResponseEntity<>("Chat successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/{idOfChat}/{idOfMessage}")
    public ResponseEntity<String> deleteMessage(@PathVariable String idOfChat, @PathVariable int idOfMessage) {
        messagingService.deleteMessage(idOfChat, idOfMessage);

        return new ResponseEntity<>("Chat successfully deleted", HttpStatus.OK);
    }

}
