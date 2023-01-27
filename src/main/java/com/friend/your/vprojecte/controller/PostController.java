package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<Page<Post>> newsFeed(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(postService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping("/like/{idOfPost}")
    public ResponseEntity<String> likePost(@PathVariable Integer idOfPost) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.like(idOfPost, login);

        return new ResponseEntity<>("Liked", HttpStatus.CREATED);
    }

    @PutMapping("/like/{idOfPost}")
    public ResponseEntity<String> removeLikeFromPost(@PathVariable Integer idOfPost) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.removeLike(idOfPost, login);

        return new ResponseEntity<>("Like removed", HttpStatus.OK);
    }

    @GetMapping("/comments/{idOfPost}")
    public ResponseEntity<Page<Comment>> getComments(
            @PathVariable Integer idOfPost,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(postService.showComments(pageNo, pageSize, idOfPost), HttpStatus.OK);
    }

    @PostMapping("/comments/{idOfPost}")
    public ResponseEntity<Comment> commentOnPost(@PathVariable Integer idOfPost, @RequestBody CommentDto commentDto) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        commentDto.setUserLogin(login);

        return new ResponseEntity<>(postService.comment(idOfPost, commentDto), HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto post) {

        return new ResponseEntity<>(postService.makePost(post), HttpStatus.CREATED);
    }

    // TODO: add support for editing end deleting comments
}
