package com.friend.your.vprojecteapiserver.controller;

import com.friend.your.vprojecteapiserver.dto.UserDto;
import com.friend.your.vprojecteapiserver.service.KCServiceImpl;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final static int KC_CONFLICT = 409;
    private final static String USER_ROLE_NAME = "user";

    private final KCServiceImpl kcService;



    @GetMapping("/vasya")
    public ResponseEntity<String> vasya() {
        return new ResponseEntity<>("zdesb bbil vasya", HttpStatus.I_AM_A_TEAPOT);
    }


    @PostMapping("/accounts")
    public ResponseEntity addUser(@RequestBody UserDto userDto, @RequestBody(required = false) Collection<String> roles) {

        if(userDto.getUserId() != null) {
            return new ResponseEntity<>("Redundant parameter: userId must be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        var createdResponse = kcService.createKeycloakUser(userDto);

        if(createdResponse.getStatus() == KC_CONFLICT) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        var userId = CreatedResponseUtil.getCreatedId(createdResponse);

        if(roles == null) {
            roles = new ArrayList<>();
        }

        roles.add(USER_ROLE_NAME);
        kcService.addRolesToKeycloakUser(userId, roles);

        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity findUser(@RequestBody String userId) {
        if(userId.isBlank()) {
            return new ResponseEntity<>("Missed parameter: user UUID required", HttpStatus.NOT_ACCEPTABLE);
        }

        UserRepresentation kcUser;

        try {
            kcUser = kcService.findKeycloakUserById(userId);
        } catch (NotFoundException exp) {
            exp.printStackTrace();
            return new ResponseEntity<>("Could not find user with id: " + userId, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(kcUser, HttpStatus.OK);
    }

    @PutMapping("/accounts")
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {
        if(userDto.getUserId() == null || userDto.getUserId().isBlank()) {
            return new ResponseEntity<>("Missed parameter: userId must be present", HttpStatus.NOT_ACCEPTABLE);
        }

        kcService.updateKeycloakUser(userDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/accounts")
    public ResponseEntity deleteUser(@RequestBody String userId) {
        if(userId.isBlank()) {
            return new ResponseEntity<>("Missed parameter: user UUID required", HttpStatus.NOT_ACCEPTABLE);
        }

        kcService.deleteKeycloakUserById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/accounts/{userId}/roles")
    public ResponseEntity addRolesToUser(@RequestParam String userId, @RequestBody Collection<String> roles) {

        kcService.addRolesToKeycloakUser(userId, roles);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
