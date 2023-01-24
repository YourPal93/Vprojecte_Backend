package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.Comment;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;


public interface PostService {

    public Page<Post> findAll(int pageNo, int pageSize);

    public void like(int idOfPost, String userLogin);

    public void removeLike(int idOfPost, String userLogin);

    public Page<Comment> showComments(int pageNo, int pageSize, int idOfPost);

    public void comment(int idOfPost, CommentDto commentDto);

    public void share(Post post, AppUser user);

    public Post makePost(Post post);
}
