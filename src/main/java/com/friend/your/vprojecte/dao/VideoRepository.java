package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Integer> {

    Optional<Video> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);

    @Query("SELECT name FROM videos")
    Page<String> getAllEntryNames(Pageable pageable);

}
