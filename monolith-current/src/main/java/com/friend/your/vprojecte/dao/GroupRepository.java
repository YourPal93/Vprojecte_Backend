package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);
    Page<Group> findByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);
    @Query("SELECT new com.friend.your.vprojecte.dto.GroupDto(g.id, g.name, g.closed) FROM groups g")
    Page<GroupDto> findAllGroupDto(Pageable pageable);
    @Query("SELECT new com.friend.your.vprojecte.dto.GroupDto(g.id, g.name, g.closed) " +
            "FROM groups g " +
            "WHERE g.name " +
            "LIKE %:name%")
    Page<GroupDto> findAllContainingGroupDto(@Param("name") String name, Pageable pageable);
}
