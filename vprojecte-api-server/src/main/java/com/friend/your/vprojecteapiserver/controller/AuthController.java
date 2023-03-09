package com.friend.your.vprojecteapiserver.controller;

import com.friend.your.vprojecteapiserver.dto.UserDto;
import com.friend.your.vprojecteapiserver.service.KCServiceImpl;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.Collections;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {

    private final static int KC_CONFLICT = 409;
    private final static String USER_ROLE_NAME = "user";

    private final KCServiceImpl kcService;

    @PostMapping("/accounts")
    public ResponseEntity createUser(@RequestBody UserDto userDto) {
        if(!userDto.getUserId().isBlank()) {
            return new ResponseEntity<>("Redundant parameter: userId must be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        var createdResponse = kcService.createKeycloakUser(userDto);

        if(createdResponse.getStatus() == KC_CONFLICT) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        var userId = CreatedResponseUtil.getCreatedId(createdResponse);

        kcService.addRolesToKeycloakUser(userId, Collections.singletonList(USER_ROLE_NAME));

        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @DeleteMapping("/accounts")
    public ResponseEntity deleteUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        kcService.deleteKeycloakUserById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
