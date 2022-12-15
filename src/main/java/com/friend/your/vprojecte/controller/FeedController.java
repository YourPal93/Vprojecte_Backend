package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.PostService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<Page<Post>> newsFeed(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(postService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping("/like/{idOfPost}")
    public ResponseEntity<String> likePost(HttpServletRequest request, @PathVariable int idOfPost) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        postService.like(idOfPost, user.getId());
        return new ResponseEntity<>("Liked", HttpStatus.CREATED);
    }

    @PutMapping("/like/{idOfPost}")
    public ResponseEntity<String> removeLikeFromPost(HttpServletRequest request, @PathVariable int idOfPost) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        postService.removeLike(idOfPost, user.getId());
        return new ResponseEntity<>("Like removed", HttpStatus.OK);
    }

    @GetMapping("/comment/{idOfPost}")
    public ResponseEntity<Page<Comment>> showComments(
            @PathVariable int idOfPost,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(postService.showComments(pageNo, pageSize, idOfPost), HttpStatus.OK);
    }

    @PostMapping("/comment/{idOfPost}")
    public ResponseEntity<String> commentOnPost(
            HttpServletRequest request, @PathVariable int idOfPost, @RequestBody String message) {
        String login = request.getUserPrincipal().getName();
        AppUser user = userService.findByLogin(login);

        postService.comment(idOfPost, user.getId(), message);
        return new ResponseEntity<>("Commented", HttpStatus.CREATED);
    }

    @PostMapping("/make")
    public ResponseEntity<String> createPost(@RequestBody Post post) {

        postService.makePost(post);

        return new ResponseEntity<>("Post created", HttpStatus.CREATED);
    }
}
