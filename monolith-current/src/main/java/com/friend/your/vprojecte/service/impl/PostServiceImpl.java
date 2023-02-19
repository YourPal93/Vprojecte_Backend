package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.CommentRepository;
import com.friend.your.vprojecte.dao.LikeRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateRepository;
import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import com.friend.your.vprojecte.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserPlateRepository userPlateRepository;
    private final LikeRepository likeRepository;

    @Override
    public Page<PostDto> findAll(int pageNo, int pageSize) {
        log.info("Requesting news feed page {} of size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return postRepository.findAllPostDto(pageable);
    }

    @Override
    public void like(Integer postId, String userLogin) {
        log.info("Adding like from user with login {} to the post {}", userLogin, postId);

        AppUserPlate userPlate = userPlateRepository.findByUserLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        Like newLike = new Like(userPlate.getUserId());

        newLike.setPostId(postId);

        likeRepository.save(newLike);
    }

    @Override
    public void removeLike(Integer postId, String userLogin) {
        log.info("Removing like of user with login {} from the post {}", userLogin, postId);

        AppUserPlate userPlate = userPlateRepository.findByUserLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        likeRepository.deleteByUserIdAndPostId(userPlate.getUserId(), postId);
    }

    @Override
    public Page<CommentDto> getComments(int pageNo, int pageSize, Integer postId) {
        log.info("Requesting comments page {} of size {} from post {}", pageNo, pageSize, postId);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return commentRepository.findAllByPostIdCommentDto(postId, pageable);
    }

    @Override
    public CommentDto addComment(Integer postId, CommentDto commentDto) {
        log.info("Adding comment from user with login {} to the post {}", commentDto.getUserLogin(), postId);

        AppUserPlate userPlate = userPlateRepository.findByUserLogin(commentDto.getUserLogin())
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Comment newComment = new Comment();

        newComment.setMessage(commentDto.getMessage());
        newComment.setCreationDate(LocalDateTime.now());
        newComment.setCreatedBy(userPlate);
        newComment.setPostId(postId);

        Comment savedComment = commentRepository.save(newComment);

        commentDto.setId(savedComment.getId());

        return commentDto;
    }

    @Override
    public void share(PostDto post, String receivingUser) {

        // TODO:PostService: share - add share option after adding messaging
    }

    @Override
    public PostDto makePost(PostDto post) {
        // TODO:PostService: makePost - add support for posting media files

        log.info("Creating new post for newsfeed");

        Post savedPost = postRepository.save(new Post(
                post.getUserLogin(),
                post.getDescription(),
                post.getUrl(),
                LocalDateTime.now()
        ));

        post.setId(savedPost.getId());
        post.setCreationDate(savedPost.getCreationDate());

        return post;
    }
}
