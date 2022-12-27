package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    void deleteByNameAndUserId(String name, int userId);
    void deleteByName(String name);
}
