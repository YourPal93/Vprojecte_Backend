package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT new com.friend.your.vprojecte.dto.PostDto(" +
            "p.id, p.userLogin, p.description, p.url, p.creationDate) " +
            "FROM posts p")
    Page<PostDto> findAllPostDto(Pageable pageable);
}
