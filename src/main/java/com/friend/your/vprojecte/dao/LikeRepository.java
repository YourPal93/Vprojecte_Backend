package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    void deleteByUserIdAndPostId(Integer userId, Integer postId);
}
