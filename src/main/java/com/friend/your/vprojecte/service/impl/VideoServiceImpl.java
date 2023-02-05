package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.VideoRepository;
import com.friend.your.vprojecte.entity.Video;
import com.friend.your.vprojecte.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;


    @Override
    public Page<String> getAllNames(int pageNo, int pageSize) {
        log.info("Requesting names of videos page {} with page size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return videoRepository.getAllEntryNames(pageable);
    }

    @Override
    public Video get(String name) throws IOException {
        log.info("Requesting video with name {}", name);

        Video video = videoRepository.findByName(name).orElseThrow(() -> new RuntimeException("Video not found"));

        return video;
    }

    @Override
    public void save(MultipartFile file, String name) throws IOException {
        log.info("Saving video with name {}", name);

        if(videoRepository.existsByName(name)) {
            throw new RuntimeException("Video with such name already exists");
        }

        Video video = new Video(name, file.getBytes());

        videoRepository.save(video);
    }

    @Override
    public void delete(String name) {
        log.info("Deleting video with name {}", name);

        videoRepository.deleteByName(name);
    }
}
