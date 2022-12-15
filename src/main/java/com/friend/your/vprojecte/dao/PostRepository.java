package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAll(Pageable pageable);

}
