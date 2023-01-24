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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class FeedController {

    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<Page<Post>> newsFeed(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(postService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping("/like/{idOfPost}")
    public ResponseEntity<String> likePost(@PathVariable int idOfPost) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.like(idOfPost, login);

        return new ResponseEntity<>("Liked", HttpStatus.CREATED);
    }

    @PutMapping("/like/{idOfPost}")
    public ResponseEntity<String> removeLikeFromPost(@PathVariable int idOfPost) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.removeLike(idOfPost, login);

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
    public ResponseEntity<String> commentOnPost(@PathVariable int idOfPost, @RequestBody Comment comment) {

        postService.comment(idOfPost, comment);

        return new ResponseEntity<>("Commented", HttpStatus.CREATED);
    }

    @PostMapping("/make")
    public ResponseEntity<String> createPost(@RequestBody Post post) {

        postService.makePost(post);

        return new ResponseEntity<>("Post created", HttpStatus.CREATED);
    }
}
