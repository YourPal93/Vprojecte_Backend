package com.friend.your.vprojecte.controller;


import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.FriendService;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("/all")
    public ResponseEntity<Page<AppUser>> findAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(userService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/my_page")
    public ResponseEntity<AppUserDto> myPage() {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @GetMapping("/find/{login}")
    public ResponseEntity<AppUserDto> findByLogin(@PathVariable String login) {
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @GetMapping("/find/{login}/match")
    public ResponseEntity<Page<AppUserPlate>> findByLoginMatch(
            @PathVariable String login,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(userService.findByLoginMatch(pageNo, pageSize, login), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUserDto user) {

        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.delete(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/friend_list")
    public ResponseEntity<Page<AppUser>> friendList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(friendService.findAllFriends(pageNo, pageSize, login), HttpStatus.OK);
    }

    @GetMapping("/friend/find/{login}")
    public ResponseEntity<AppUser> findFriend(@PathVariable String login) {
        return new ResponseEntity<>(friendService.findFriend(login), HttpStatus.OK);
    }

    @GetMapping("/friend/find/{login}/match")
    public ResponseEntity<Page<AppUserPlate>> findFriendsMatch(
            @PathVariable String login,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(friendService.findFriendsMatch(pageNo, pageSize, login), HttpStatus.OK);
    }

    @PostMapping("/friend/add/{id}")
    public ResponseEntity<String> addFriend(@PathVariable int id) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        friendService.addFriend(login, id);
        return new ResponseEntity<>("Friend has been added", HttpStatus.CREATED);
    }

    @DeleteMapping("/friend/delete/{id}")
    public ResponseEntity<String> deleteFriend(@PathVariable int id) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        friendService.deleteFriend(login, id);
        return new ResponseEntity<>("Friend has been deleted", HttpStatus.OK);
    }

    @GetMapping("/communication")
    public ResponseEntity<Page<Chat>> chatLogs(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.getChatLogs(pageNo, pageSize, login), HttpStatus.OK);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostDto>> getUserWall(
            @PathVariable int userId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(userService.getUserWall(pageNo, pageSize, userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<String> addPostToUser(@PathVariable int userId, @RequestBody Post post) {

        userService.addPostToUser(post, userId);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
