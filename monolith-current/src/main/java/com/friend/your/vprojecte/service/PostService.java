package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;


public interface PostService {

    public Page<PostDto> findAll(int pageNo, int pageSize);

    public void like(Integer postId, String userLogin);

    public void removeLike(Integer postId, String userLogin);

    public Page<CommentDto> getComments(int pageNo, int pageSize, Integer postId);

    public CommentDto addComment(Integer postId, CommentDto commentDto);

    public void share(PostDto post, String receivingUser);

    public PostDto makePost(PostDto post);
}
