package com.friend.your.vprojecte.controller;


import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.service.FriendService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
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

    // TODO: UserController: refactor /find endpoints to return pages of matched results

    @GetMapping("/my_page")
    public ResponseEntity<AppUser> myPage(HttpServletRequest request) {
        String login = request.getUserPrincipal().getName();
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @GetMapping("/find/{login}")
    public ResponseEntity<AppUser> findByLogin(@PathVariable String login) {
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.delete(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/friend_list")
    public ResponseEntity<Page<AppUser>> friendList(
            HttpServletRequest request,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);
        return new ResponseEntity<>(friendService.findAllFriends(pageNo, pageSize, user), HttpStatus.OK);
    }

    @GetMapping("/friend/find/{login}")
    public ResponseEntity<AppUser> findFriend(@PathVariable String login) {
        return new ResponseEntity<>(friendService.findFriend(login), HttpStatus.OK);
    }

    @PostMapping("/friend/add/{id}")
    public ResponseEntity<String> addFriend(HttpServletRequest request, @PathVariable int id) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        friendService.addFriend(user, id);
        return new ResponseEntity<>("Friend has been added", HttpStatus.CREATED);
    }

    @DeleteMapping("/friend/delete/{id}")
    public ResponseEntity<String> deleteFriend(HttpServletRequest request, @PathVariable int id) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        friendService.deleteFriend(user, id);
        return new ResponseEntity<>("Friend has been deleted", HttpStatus.OK);
    }
}
