package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.*;
import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    private UserPlateRepository userPlateRepository;
    @Mock
    private LikeRepository likeRepository;
    @InjectMocks
    private PostServiceImpl underTest;

    @Test
    void findAll_GivenValidPageNoAndPageSize_ShouldReturnPageOfPostDtos() {
        Page<PostDto> testPostsPage = mock(Page.class);

        when(postRepository.findAllPostDto(any(Pageable.class))).thenReturn(testPostsPage);

        underTest.findAll(0, 10);

        verify(postRepository).findAllPostDto(any(Pageable.class));
    }

    @Test
    void like_GivenValidPostId_ShouldNotReturnAnything() {

        when(likeRepository.save(any(Like.class))).thenReturn(mock(Like.class));
        when(userPlateRepository.findByUserLogin(anyString())).thenReturn(Optional.of(mock(AppUserPlate.class)));


        underTest.like(1, "TEST_LOGIN");

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void removeLike_GivenValidPostId_ShouldNotReturnAnything() {

        doNothing().when(likeRepository).deleteByUserIdAndPostId(anyInt(), anyInt());
        when(userPlateRepository.findByUserLogin(anyString())).thenReturn(Optional.of(mock(AppUserPlate.class)));

        underTest.removeLike(1, "TEST_LOGIN");

        verify(likeRepository).deleteByUserIdAndPostId(anyInt(), anyInt());
    }

    @Test
    void showComments_GivenValidPostId_ShouldReturnPageOfComments() {
        Page<CommentDto> testCommentsPage = mock(Page.class);

        when(commentRepository.findAllByPostIdCommentDto(anyInt(), any(Pageable.class))).thenReturn(testCommentsPage);

        Page<CommentDto> returnedPage = underTest.getComments(0, 10, 1);

        assertEquals(testCommentsPage, returnedPage);
    }

    @Test
    void addComment_GivenValidPostIdAndValidComment_ShouldReturnCommentDto() {
        CommentDto testCommentDto = new CommentDto();

        testCommentDto.setUserLogin("TEST_LOGIN");

        when(userPlateRepository.findByUserLogin(anyString())).thenReturn(Optional.of(mock(AppUserPlate.class)));
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));

        underTest.addComment(1, testCommentDto);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void makePost_GivenValidPostDto_ShouldReturnPostDto() {
        PostDto testPostDto = mock(PostDto.class);

        when(postRepository.save(any(Post.class))).thenReturn(mock(Post.class));

        underTest.makePost(testPostDto);

        verify(postRepository).save(any(Post.class));
    }
}