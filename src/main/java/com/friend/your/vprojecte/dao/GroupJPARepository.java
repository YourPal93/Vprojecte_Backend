package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJPARepository extends JpaRepository<Group, Integer> {

    Page<Group> findByNameContaining(String name, Pageable pageable);
}
