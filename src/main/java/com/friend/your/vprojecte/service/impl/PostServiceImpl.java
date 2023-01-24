package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.CommentRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.dao.UserPlateJPARepository;
import com.friend.your.vprojecte.dto.CommentDto;
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

    @Override
    public Page<Post> findAll(int pageNo, int pageSize) {
        log.info("Requesting news feed page {} of size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return postRepository.findAll(pageable);
    }

    @Override
    public void like(int idOfPost, String userLogin) {
        log.info("Adding like from user with login {} to the post {}", userLogin, idOfPost);


        Post post = postRepository.findById(idOfPost).orElseThrow(() -> new RuntimeException("Post not found"));
        AppUserPlate userPlate = userPlateRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));

        post.getLikes().add(new Like(userPlate.getUserId()));
        postRepository.save(post);
    }

    @Override
    public void removeLike(int idOfPost, String userLogin) {
        log.info("Removing like of user with login {} from the post {}", userLogin, idOfPost);

        AppUserPlate userPlate = userPlateRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Like like = new Like(userPlate.getUserId());
        Post post = postRepository.findById(idOfPost).orElseThrow(() -> new RuntimeException("Post not found"));

        post.getLikes().remove(like);
        postRepository.save(post);
    }

    @Override
    public Page<Comment> showComments(int pageNo, int pageSize, int idOfPost) {
        log.info("Requesting comments page {} of size {} from post {}", pageNo, pageSize, idOfPost);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return commentRepository.findAllByPostId(idOfPost, pageable);
    }

    @Override
    public void comment(int idOfPost, CommentDto commentDto) {
        log.info("Adding comment from user with login {} to the post {}", commentDto.getUserLogin(), idOfPost);

        Post post = postRepository.findById(idOfPost).orElseThrow(() -> new RuntimeException("Post not found"));
        AppUserPlate userPlate = userPlateRepository.findByLogin(commentDto.getUserLogin())
                .orElseThrow(() -> new RuntimeException("User plate not found"));
        Comment newComment = new Comment(commentDto.getId(), commentDto.getMessage());

        newComment.setCreatedBy(userPlate);
        if(commentDto.getId() == null) {
            newComment.setCreationDate(LocalDateTime.now());
        } else {
            newComment.setCreationDate(commentDto.getCreatedDate());
        }

        post.getComments().add(newComment);
    }

    @Override
    public void share(Post post, AppUser receivingUser) {

        // TODO:PostService: share - add share option after adding messaging
    }

    @Override
    public Post makePost(Post post) {
        // TODO:PostService: makePost - add support for sharing media files

        return postRepository.save(post);
    }
}
