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
@RequestMapping("/groups/administration")
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

    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Integer groupId) {
        adminService.deleteGroup(groupId);

        return new ResponseEntity<>("Group was successfully deleted", HttpStatus.OK);
    }

    @PutMapping("/{groupId}/moderation/{memberId}")
    public ResponseEntity<String> setModerator(@PathVariable Integer groupId, @PathVariable Integer memberId) {
        adminService.setModerator(groupId, memberId);

        return new ResponseEntity<>("Moderator was successfully added", HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/moderation/{memberId}")
    public ResponseEntity<String> deleteModerator(@PathVariable Integer groupId, @PathVariable Integer memberId) {

        adminService.deleteModerator(groupId, memberId);

        return new ResponseEntity<>("Moderator was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{groupId}/{memberId}")
    public ResponseEntity<String> setAdmin(@PathVariable Integer groupId, @PathVariable Integer memberId) {

        String adminLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        adminService.setAdmin(adminLogin, memberId, groupId);

        return new ResponseEntity<>("Admin was set successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Integer groupId, @PathVariable Integer memberId) {

        adminService.deleteMember(memberId, groupId);

        return new ResponseEntity<>("Member was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/requests")
    public ResponseEntity<String> approveMembershipRequest(@RequestBody AddRequest request) {

        adminService.approveMembershipRequest(request);

        return new ResponseEntity<>("Membership request has been approved", HttpStatus.OK);
    }

    @DeleteMapping("/requests")
    public ResponseEntity<String> denyMembershipRequest(@RequestBody AddRequest request) {
        adminService.denyMembershipRequest(request);

        return new ResponseEntity<>("Request has been denied", HttpStatus.OK);
    }

    @DeleteMapping("/feed/{postId}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable Integer postId) {
        adminService.deletePost(postId);

        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }


}
