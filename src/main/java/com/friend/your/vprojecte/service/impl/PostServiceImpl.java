package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.CommentRepository;
import com.friend.your.vprojecte.dao.LikeJpaRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserPlateJPARepository userPlateRepository;
    private final LikeJpaRepository likeRepository;

    @Override
    public Page<Post> findAll(int pageNo, int pageSize) {
        log.info("Requesting news feed page {} of size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return postRepository.findAll(pageable);
    }

    @Override
    public void like(Integer idOfPost, String userLogin) {
        log.info("Adding like from user with login {} to the post {}", userLogin, idOfPost);

        AppUserPlate userPlate = userPlateRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        Like newLike = new Like(userPlate.getUserId());

        newLike.setPostId(idOfPost);

        likeRepository.save(newLike);
    }

    @Override
    public void removeLike(Integer idOfPost, String userLogin) {
        log.info("Removing like of user with login {} from the post {}", userLogin, idOfPost);

        AppUserPlate userPlate = userPlateRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        likeRepository.deleteByUserIdAndPostId(userPlate.getUserId(), idOfPost);
    }

    @Override
    public Page<Comment> showComments(int pageNo, int pageSize, Integer idOfPost) {
        log.info("Requesting comments page {} of size {} from post {}", pageNo, pageSize, idOfPost);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return commentRepository.findAllByPostId(idOfPost, pageable);
    }

    @Override
    public Comment comment(Integer idOfPost, CommentDto commentDto) {
        log.info("Adding comment from user with login {} to the post {}", commentDto.getUserLogin(), idOfPost);

        Post post = postRepository.findById(idOfPost).orElseThrow(() -> new RuntimeException("Post not found"));
        AppUserPlate userPlate = userPlateRepository.findByLogin(commentDto.getUserLogin())
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Comment newComment = new Comment();

        newComment.setMessage(commentDto.getMessage());
        newComment.setCreationDate(LocalDateTime.now());
        newComment.setCreatedBy(userPlate);
        newComment.setPostId(idOfPost);

        return commentRepository.save(newComment);
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
