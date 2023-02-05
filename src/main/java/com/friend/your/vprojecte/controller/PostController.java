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
    public ResponseEntity<Page<PostDto>> newsFeed(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {

        return new ResponseEntity<>(postService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> likePost(@PathVariable Integer postId) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.like(postId, login);

        return new ResponseEntity<>("Liked", HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<String> removeLikeFromPost(@PathVariable Integer postId) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        postService.removeLike(postId, login);

        return new ResponseEntity<>("Like removed", HttpStatus.OK);
    }

    @GetMapping("/{poseId}/comments")
    public ResponseEntity<Page<CommentDto>> getComments(
            @PathVariable Integer poseId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(postService.getComments(pageNo, pageSize, poseId), HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> commentOnPost(@PathVariable Integer postId, @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(postService.addComment(postId, commentDto), HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto post) {

        return new ResponseEntity<>(postService.makePost(post), HttpStatus.CREATED);
    }

    // TODO: add support for editing end deleting comments
}
