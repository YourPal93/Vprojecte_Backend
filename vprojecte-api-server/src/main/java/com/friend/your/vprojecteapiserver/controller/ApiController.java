package com.friend.your.vprojecteapiserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/vasya")
    public ResponseEntity<String> vasya() {
        return new ResponseEntity<>("zdesb bbil vasya", HttpStatus.I_AM_A_TEAPOT);
    }
}
