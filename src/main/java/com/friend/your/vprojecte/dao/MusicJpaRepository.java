package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MusicJpaRepository extends JpaRepository<Song, Long> {

    Optional<Song> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

    @Query("SELECT name FROM songs")
    Page<String> getAllNames(Pageable pageable);
}
