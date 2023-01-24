package com.friend.your.vprojecte.service;

import com.friend.your.vprojecte.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {

    Page<String> getAllNames(int pageNo, int pageSize);

    Video get(String name) throws IOException;

    void save(MultipartFile file, String name) throws IOException;

    void delete(String name);
}
