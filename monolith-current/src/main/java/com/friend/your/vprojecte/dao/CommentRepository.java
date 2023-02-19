package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.CommentDto;
import com.friend.your.vprojecte.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByPostId(Integer postId, Pageable pageable);

    @Query("SELECT new com.friend.your.vprojecte.dto.CommentDto(" +
            "c.id, c.message, c.createdBy.userLogin, c.creationDate) " +
            "FROM comments c " +
            "WHERE postId = :postId")
    Page<CommentDto>  findAllByPostIdCommentDto(@Param("postId") Integer postId, Pageable pageable);
}
