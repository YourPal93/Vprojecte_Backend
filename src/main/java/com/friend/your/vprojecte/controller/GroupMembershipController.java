package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.GroupMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupMembershipController {

    private final GroupMembershipService membershipService;
    @GetMapping("/")
    public ResponseEntity<Page<GroupDto>> findAll(
            @RequestParam(name = "PageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "PageSize", defaultValue = "10", required = false) int pageSize) {

        return new ResponseEntity<>(membershipService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{groupName}/match")
    public ResponseEntity<Page<GroupDto>> findGroupMatch(
            @PathVariable String groupName,
            @RequestParam(name = "PageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "PageSize", defaultValue = "10", required = false) int pageSize
            ) {

        return new ResponseEntity<>(membershipService.findByNameMatch(pageNo,pageSize, groupName), HttpStatus.OK);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Role> joinGroup(@PathVariable Integer groupId) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(membershipService.joinGroup(groupId, userLogin), HttpStatus.CREATED);
    }

    @PostMapping("/requests")
    public ResponseEntity<String> sendMembershipRequest(@RequestBody AddRequest request) {

        membershipService.sendMembershipRequest(request);

        return new ResponseEntity<>("Membership request has been sent", HttpStatus.CREATED);
    }

    @PostMapping("/{groupId}/feed")
    public ResponseEntity<PostDto> makeGroupPost(@PathVariable Integer groupId, @RequestBody Post post) {

        return new ResponseEntity<>(membershipService.makePost(groupId, post), HttpStatus.CREATED);
    }

}
