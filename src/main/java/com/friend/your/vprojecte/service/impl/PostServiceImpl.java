package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.CommentRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Like;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Page<Post> findAll(int pageNo, int pageSize) {
        log.info("Requesting news feed page {} of size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return postRepository.findAll(pageable);
    }

    @Override
    public void like(int idOfPost, int idOfUser) {
        log.info("Adding like from user with id {} to the post {}", idOfUser, idOfPost);

        Optional<Post> post = postRepository.findById(idOfPost);
        post.ifPresent(value -> value.getLikes().add(new Like(idOfUser)));
        postRepository.save(post.get());
    }

    @Override
    public void removeLike(int idOfPost, int idOfUser) {
        log.info("Removing like of user with id {} from the post {}", idOfUser, idOfPost);

        Like like = new Like(idOfUser);
        Optional<Post> post = postRepository.findById(idOfPost);
        post.ifPresent(value -> value.getLikes().remove(like));
        postRepository.save(post.get());
    }

    @Override
    public Page<Comment> showComments(int pageNo, int pageSize, int idOfPost) {
        log.info("Requesting comments page {} of size {} from post {}", pageNo, pageSize, idOfPost);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return commentRepository.findAllByPostId(idOfPost, pageable);
    }

    @Override
    public void comment(int idOfPost, int idOfUser, String message) {
        log.info("Adding comment from user {} to the post {}", idOfUser, idOfPost);

        Optional<Post> post = postRepository.findById(idOfPost);
        post.ifPresent(value -> value.getComments().add(new Comment(message, idOfUser)));
    }

    @Override
    public void share(Post post, AppUser receivingUser) {

        // TODO: PostService: share - add share option after adding messaging
    }

    @Override
    public Post makePost(Post post) {
        // TODO: PostService: makePost - add support for sharing media files

        return postRepository.save(post);
    }
}
