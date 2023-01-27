package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;


public interface PostService {

    public Page<Post> findAll(int pageNo, int pageSize);

    public void like(Integer idOfPost, String userLogin);

    public void removeLike(Integer idOfPost, String userLogin);

    public Page<Comment> showComments(int pageNo, int pageSize, Integer idOfPost);

    public Comment comment(Integer idOfPost, CommentDto commentDto);

    public void share(PostDto post, String receivingUser);

    public PostDto makePost(PostDto post);
}
