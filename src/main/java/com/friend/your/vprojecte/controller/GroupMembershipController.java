package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AddRequest;
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
    public ResponseEntity<Group> findGroup(int idOfGroup) {

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
    public ResponseEntity<String> joinGroup(@PathVariable int idOfGroup) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        membershipService.addMember(idOfGroup, userLogin);

        return new ResponseEntity<>("Successfully joined the group", HttpStatus.CREATED);
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendMembershipRequest(@RequestBody AddRequest request) {
        membershipService.sendMembershipRequest(request);

        return new ResponseEntity<>("Request has been sent", HttpStatus.CREATED);
    }

    @PostMapping("/{idOfGroup}/feed")
    public ResponseEntity<String> makeGroupPost(@PathVariable int idOfGroup, @RequestBody Post post) {
        membershipService.makePost(idOfGroup, post);

        return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
    }

}
