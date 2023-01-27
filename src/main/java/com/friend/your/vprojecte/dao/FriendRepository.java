package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Page<Friend> findAll(Pageable pageable);
}
