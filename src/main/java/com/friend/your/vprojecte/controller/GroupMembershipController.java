package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AddRequest;
import com.friend.your.vprojecte.entity.AppUserPlate;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.entity.Post;
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
@RequestMapping("/group")
public class GroupMembershipController {

    private final GroupMembershipService membershipService;
    @GetMapping("/")
    public ResponseEntity<Page<Group>> findAll(
            @RequestParam(name = "PageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "PageSize", defaultValue = "10", required = false) int pageSize) {

        return new ResponseEntity<>(membershipService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{idOfGroup}")
    public ResponseEntity<Group> findGroup(Integer idOfGroup) {

        return new ResponseEntity<>(membershipService.findGroup(idOfGroup), HttpStatus.OK);
    }

    @GetMapping("/{nameOfGroup}/match")
    public ResponseEntity<Page<Group>> findGroupMatch(
            @PathVariable String nameOfGroup,
            @RequestParam(name = "PageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "PageSize", defaultValue = "10", required = false) int pageSize
            ) {

        return new ResponseEntity<>(membershipService.findByNameMatch(pageNo,pageSize, nameOfGroup), HttpStatus.OK);
    }

    @PostMapping("/{idOfGroup}/members")
    public ResponseEntity<AppUserPlate> joinGroup(@PathVariable Integer idOfGroup) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(membershipService.addMember(idOfGroup, userLogin), HttpStatus.CREATED);
    }

    @PostMapping("/request")
    public ResponseEntity<AddRequest> sendMembershipRequest(@RequestBody AddRequest request) {

        return new ResponseEntity<>(membershipService.sendMembershipRequest(request), HttpStatus.CREATED);
    }

    @PostMapping("/{idOfGroup}/feed")
    public ResponseEntity<PostDto> makeGroupPost(@PathVariable Integer idOfGroup, @RequestBody Post post) {

        return new ResponseEntity<>(membershipService.makePost(idOfGroup, post), HttpStatus.CREATED);
    }

}
