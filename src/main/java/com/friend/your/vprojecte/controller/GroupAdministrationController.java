package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AddRequest;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.service.GroupAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group/administration")
public class GroupAdministrationController {

    private final GroupAdministrationService adminService;

    @PostMapping("/")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(adminService.createGroup(login, group), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) {
        adminService.updateGroup(group);

        return new ResponseEntity<>(adminService.updateGroup(group), HttpStatus.OK);
    }

    @DeleteMapping("/{idOfGroup}")
    public ResponseEntity<String> deleteGroup(@PathVariable int idOfGroup) {
        adminService.deleteGroup(idOfGroup);

        return new ResponseEntity<>("Group was successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/moderation/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> setModerator(@PathVariable String nameOfGroup, @PathVariable int idOfMember) {
        adminService.setModerator(nameOfGroup, idOfMember);

        return new ResponseEntity<>("Moderator was successfully added", HttpStatus.CREATED);
    }

    @DeleteMapping("/moderation/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> deleteModerator(@PathVariable String nameOfGroup, @PathVariable int idOfMember) {

        adminService.deleteModerator(nameOfGroup, idOfMember);

        return new ResponseEntity<>("Moderator was deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> setAdmin(@PathVariable String nameOfGroup, @PathVariable int idOfMember) {

        String adminLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        adminService.setAdmin(adminLogin, nameOfGroup, idOfMember);

        return new ResponseEntity<>("Admin was set successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{idOfGroup}/{idOfMember}")
    public ResponseEntity<String> deleteMember(@PathVariable int idOfGroup, @PathVariable int idOfMember) {
        adminService.deleteMember(idOfGroup, idOfMember);

        return new ResponseEntity<>("Member was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/request")
    public ResponseEntity<String> approveMembershipRequest(@RequestBody AddRequest request) {
        adminService.approveMembershipRequest(request);

        return new ResponseEntity<>("Request has been approved", HttpStatus.OK);
    }

    @DeleteMapping("/request")
    public ResponseEntity<String> denyMembershipRequest(@RequestBody AddRequest request) {
        adminService.denyMembershipRequest(request);

        return new ResponseEntity<>("Request has been denied", HttpStatus.OK);
    }

    @DeleteMapping("/feed/{idOfPost}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable int idOfPost) {
        adminService.deletePost(idOfPost);

        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }


}
