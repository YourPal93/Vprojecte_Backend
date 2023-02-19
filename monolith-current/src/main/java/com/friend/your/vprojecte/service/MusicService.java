package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MusicService {

    Page<String> getAllNames(int pageNo, int pageSize);

    Song get(String name);

    void save(MultipartFile file, String name) throws IOException;

    void delete(String name);
}
