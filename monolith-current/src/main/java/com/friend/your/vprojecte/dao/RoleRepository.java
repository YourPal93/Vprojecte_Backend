package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    Set<Role> findByUserIdAndRoleType(Integer userId, Integer roleType);
    void deleteByNameAndUserId(String name, int userId);
    void deleteByName(String name);

    boolean existsByNameAndUserId(String roleName, Integer userId);

    boolean existsByName(String name);

    Optional<Role> findByNameAndUserId(String roleName, Integer userId);
}
