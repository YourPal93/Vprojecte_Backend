package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Page<AppUser> findByIdIn(Collection<Integer> users, Pageable pageable);
    Optional<AppUser> findByLogin(String login);
    Boolean existsByLogin(String login);
}
