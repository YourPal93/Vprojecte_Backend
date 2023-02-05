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

        return new ResponseEntity<>(userService.findAllUsers(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/my_page")
    public ResponseEntity<AppUserDto> myPage() {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.findUser(login), HttpStatus.OK);
    }

    @GetMapping("/{login}")
    public ResponseEntity<AppUserDto> findByLogin(@PathVariable String login) {
        return new ResponseEntity<>(userService.findUser(login), HttpStatus.OK);
    }

    @GetMapping("/{login}/match")
    public ResponseEntity<Page<AppUserPlate>> findByLoginMatch(
            @PathVariable String login,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(userService.findByLoginMatch(pageNo, pageSize, login), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<AppUserDto> updateUser(@RequestBody AppUserDto user) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(userService.updateUser(userLogin, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/{userLogin}/friend_list")
    public ResponseEntity<Page<AppUserPlate>> userFriendList(
            @PathVariable String userLogin,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(friendService.getUserFriendList(pageNo, pageSize, userLogin), HttpStatus.OK);
    }

    @GetMapping("/friends/{friendLogin}")
    public ResponseEntity<AppUserPlate> findFriend(@PathVariable String friendLogin) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(friendService.findFriend(userLogin, friendLogin), HttpStatus.OK);
    }

    @GetMapping("/friends/{friendLogin}/match")
    public ResponseEntity<Page<AppUserPlate>> findFriendsMatch(
            @PathVariable String friendLogin,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(
                friendService.findFriendsMatch(pageNo, pageSize, userLogin, friendLogin), HttpStatus.OK);
    }

    @PostMapping("/friends")
    public ResponseEntity<String> addFriend(@RequestBody AppUserPlate userPlateToAdd) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        friendService.addFriendToUser(userLogin, userPlateToAdd);
        return new ResponseEntity<>("Friend has been added", HttpStatus.OK);
    }

    @DeleteMapping("/friends/{id}")
    public ResponseEntity<String> deleteFriend(@PathVariable Integer id) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        friendService.deleteFriend(login, id);
        return new ResponseEntity<>("Friend has been deleted", HttpStatus.OK);
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
    public ResponseEntity<PostDto> addPostToUser(@PathVariable Integer userId, @RequestBody Post post) {

        return new ResponseEntity<>(userService.addPostToUser(post, userId), HttpStatus.CREATED);
    }
}
