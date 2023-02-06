package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.AppUserCredentialsDto;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.service.RegistrationService;
import com.friend.your.vprojecte.utility.JwtUtil;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUserDto userDto) {
        // TODO: ApiController: register - add validation via email

        if(userService.userExists(userDto.getLogin(), userDto.getEmail())) {
            return new ResponseEntity<>("User with the same login or email already exists", HttpStatus.BAD_REQUEST);
        }
//        if(registrationService.tokenTaken(userDto.getLogin(), userDto.getEmail())) {
//            return new ResponseEntity<>("User with the same login or email already exists", HttpStatus.BAD_REQUEST);
//        }

        registrationService.register(userDto);

        return new ResponseEntity<>("Registration successful", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUserCredentialsDto userCredentials) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userCredentials.getUsername(),
                                    userCredentials.getPassword())
                    );

            User user = (User) auth.getPrincipal();
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtUtil.generateToken(user))
                    .body(Collections.singletonMap("username", userCredentials.getUsername()));

        } catch (BadCredentialsException exp) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/account")
    public ResponseEntity<Void> activateAccount(@RequestParam("token") String tokenCode) {
        registrationService.activateAccount(tokenCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:3000/login");

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // TODO: ApiController - add support for account recovery


}
