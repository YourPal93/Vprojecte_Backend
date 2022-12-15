package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> add(@RequestBody AppUser user) {
        if(userService.exist(user.getLogin())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        userService.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }


}
