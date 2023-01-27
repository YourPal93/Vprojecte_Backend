package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.AddRequest;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.service.GroupAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto group) {

        return new ResponseEntity<>(adminService.updateGroup(group), HttpStatus.OK);
    }

    @DeleteMapping("/{idOfGroup}")
    public ResponseEntity<String> deleteGroup(@PathVariable Integer idOfGroup) {
        adminService.deleteGroup(idOfGroup);

        return new ResponseEntity<>("Group was successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/moderation/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> setModerator(@PathVariable String nameOfGroup, @PathVariable Integer idOfMember) {
        adminService.setModerator(nameOfGroup, idOfMember);

        return new ResponseEntity<>("Moderator was successfully added", HttpStatus.CREATED);
    }

    @DeleteMapping("/moderation/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> deleteModerator(@PathVariable String nameOfGroup, @PathVariable Integer idOfMember) {

        adminService.deleteModerator(nameOfGroup, idOfMember);

        return new ResponseEntity<>("Moderator was deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/{nameOfGroup}/{idOfMember}")
    public ResponseEntity<String> setAdmin(@PathVariable String nameOfGroup, @PathVariable Integer idOfMember) {

        String adminLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        adminService.setAdmin(adminLogin, nameOfGroup, idOfMember);

        return new ResponseEntity<>("Admin was set successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{idOfGroup}/{idOfMember}")
    public ResponseEntity<String> deleteMember(@PathVariable Integer idOfGroup, @PathVariable Integer idOfMember) {
        adminService.deleteMember(idOfGroup, idOfMember);

        return new ResponseEntity<>("Member was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/request")
    public ResponseEntity<AppUserPlate> approveMembershipRequest(@RequestBody AddRequest request) {

        return new ResponseEntity<>(adminService.approveMembershipRequest(request), HttpStatus.OK);
    }

    @DeleteMapping("/request")
    public ResponseEntity<String> denyMembershipRequest(@RequestBody AddRequest request) {
        adminService.denyMembershipRequest(request);

        return new ResponseEntity<>("Request has been denied", HttpStatus.OK);
    }

    @DeleteMapping("/feed/{idOfPost}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable Integer idOfPost) {
        adminService.deletePost(idOfPost);

        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }


}
