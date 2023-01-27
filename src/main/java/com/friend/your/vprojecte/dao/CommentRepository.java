package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    public Page<Comment> findAllByPostId(int id, Pageable pageable);
}
