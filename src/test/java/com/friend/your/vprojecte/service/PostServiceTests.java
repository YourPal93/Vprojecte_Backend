package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dao.CommentRepository;
import com.friend.your.vprojecte.dao.PostRepository;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Like;
import com.friend.your.vprojecte.entity.Post;
import com.friend.your.vprojecte.service.impl.PostServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void PostService_FindAll_ReturnPageOfPosts() {
        Page<Post> mockPosts = Mockito.mock(Page.class);

        when(postRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockPosts);

        Page<Post> pageOfPosts = postService.findAll(1, 10);

        Assertions.assertThat(pageOfPosts).isNotNull();
    }

    @Test
    public void PostService_MakePost_ReturnPost() {
        Post mockPost = Mockito.mock(Post.class);

        when(postRepository.save(Mockito.any(Post.class))).thenReturn(mockPost);

        Post savedPost = postService.makePost(mockPost);

        Assertions.assertThat(savedPost).isNotNull();
    }

    @Test
    public void PostService_Like_ReturnPost() {
        Post mockPost = new Post(
                1,
                "Test post",
                "without url",
                LocalDate.of(1111, 11 , 11));
        mockPost.setLikes(new HashSet<>());
        Like mockLike = new Like(2);

        when(postRepository.findById(1)).thenReturn(Optional.ofNullable(mockPost));
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(mockPost);

        postService.like(1, 2);

        Assertions.assertThat(mockPost.getLikes().contains(mockLike)).isTrue();
    }

    @Test
    public void PostService_RemoveLike_ReturnPost() {
        Post mockPost = new Post(
                1,
                "Test post",
                "without url",
                LocalDate.of(1111, 11 , 11));
        mockPost.setLikes(new HashSet<>());
        Like mockLike = new Like(2);
        mockPost.getLikes().add(mockLike);

        when(postRepository.findById(1)).thenReturn(Optional.ofNullable(mockPost));
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(mockPost);

        postService.removeLike(1, 2);

        Assertions.assertThat(mockPost.getLikes().contains(mockLike)).isFalse();
    }

    @Test
    public void PostService_ShowComments_ReturnPageOfComments() {
        Page<Comment> mockComments = Mockito.mock(Page.class);
        Post mockPost = Mockito.mock(Post.class);

        when(commentRepository.findAllByPostId(Mockito.anyInt(), Mockito.any(Pageable.class))).thenReturn(mockComments);

        Page<Comment> pageOfComments = postService.showComments(1, 10, 1);

        Assertions.assertThat(pageOfComments).isNotNull();
    }

    @Test
    public void PostService_Comment_ReturnPost() {
        Post mockPost = new Post(
                1,
                "Test post",
                "without url",
                LocalDate.of(1111, 11 , 11));
        Comment mockComment = new Comment("Test comment", 2);

        mockPost.setComments(new ArrayList<>());

        when(postRepository.findById(1)).thenReturn(Optional.ofNullable(mockPost));

        postService.comment(1, 2, "Test comment");

        Assertions.assertThat(mockPost.getComments().contains(mockComment)).isTrue();
    }
}
