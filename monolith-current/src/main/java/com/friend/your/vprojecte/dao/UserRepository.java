package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.dto.PostDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByLogin(String login);
    boolean existsByLogin(String userLogin);
    boolean existsByEmail(String userEmail);

    @Query("SELECT uf " +
            "FROM users u " +
            "JOIN u.friendList uf " +
            "WHERE u.login = :userLogin")
    Page<AppUserPlate> getFriendList(@Param("userLogin") String userLogin, Pageable pageable);

    @Query("SELECT uf " +
            "FROM users u " +
            "JOIN u.friendList uf " +
            "WHERE u.login = :userLogin " +
            "AND uf.userLogin LIKE %:friendLogin%")
    Page<AppUserPlate> findFriendsMatching(
            @Param("userLogin") String userLogin,
            @Param("friendLogin") String friendLogin,
            Pageable pageable);
    @Query("SELECT uf " +
            "FROM users u " +
            "JOIN u.friendList uf " +
            "WHERE u.login = :userLogin " +
            "AND uf.userLogin = :friendLogin")
    Optional<AppUserPlate> findFriend(@Param("userLogin") String userLogin, @Param("friendLogin") String friendLogin);

    @Query("SELECT new com.friend.your.vprojecte.dto.GroupDto(ug.id, ug.name, ug.closed) " +
            "FROM users u " +
            "JOIN u.groups ug " +
            "WHERE u.login = :userLogin")
    Page<GroupDto> findUserGroups(@Param("userLogin") String userLogin, Pageable pageable);

    @Query("SELECT new com.friend.your.vprojecte.dto.PostDto(" +
            "up.id, up.userLogin, up.description, up.url, up.creationDate) " +
            "FROM users u " +
            "JOIN u.posts up " +
            "WHERE u.id = :userId")
    Page<PostDto> getUserPosts(@Param("userId") Integer userId, Pageable pageable);

}
