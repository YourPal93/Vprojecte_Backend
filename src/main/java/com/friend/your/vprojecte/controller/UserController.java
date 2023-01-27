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


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("/all")
    public ResponseEntity<Page<AppUserPlate>> findAll(
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
    public ResponseEntity<AppUserDto> updateUser(@RequestBody AppUserDto user) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.update(userLogin, user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.delete(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/friend_list")
    public ResponseEntity<Page<AppUserPlate>> friendList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(friendService.findAllFriends(pageNo, pageSize, login), HttpStatus.OK);
    }

    @GetMapping("/friend/find/{login}")
    public ResponseEntity<AppUserPlate> findFriend(@PathVariable String login) {
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
    public ResponseEntity<String> addFriend(@PathVariable Integer id) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        friendService.addFriend(login, id);
        return new ResponseEntity<>("Friend has been added", HttpStatus.CREATED);
    }

    @DeleteMapping("/friend/delete/{id}")
    public ResponseEntity<String> deleteFriend(@PathVariable Integer id) {

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
            @PathVariable Integer userId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(userService.getUserWall(pageNo, pageSize, userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<String> addPostToUser(@PathVariable Integer userId, @RequestBody Post post) {

        userService.addPostToUser(post, userId);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
