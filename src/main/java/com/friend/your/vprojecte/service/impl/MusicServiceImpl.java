package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.MusicRepository;
import com.friend.your.vprojecte.entity.Song;
import com.friend.your.vprojecte.service.MusicService;
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
@RequiredArgsConstructor
@Transactional
@Service
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;


    @Override
    public Page<String> getAllNames(int pageNo, int pageSize) {
        log.info("Requesting names of songs page {} page size {}", pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return musicRepository.getAllNames(pageable);
    }

    @Override
    public Song get(String name) {
        log.info("requesting song with name {}", name);

        Song song = musicRepository.findByName(name).orElseThrow(() -> new RuntimeException("Song not found"));

        return song;
    }


    @Override
    public void save(MultipartFile file, String name) throws IOException {
        log.info("Saving song with name {}", name);

        if(musicRepository.existsByName(name)) {
            throw new RuntimeException("Song with such name already exists");
        }

        Song song = new Song(name, file.getBytes());
    }

    @Override
    public void delete(String name) {
        log.info("Deleting song with name {}", name);

        musicRepository.deleteByName(name);
    }
}
