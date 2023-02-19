package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/all")
    public ResponseEntity<Page<String>> getAllSongs(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(musicService.getAllNames(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{songName}")
    public ResponseEntity<Resource> getSong(@PathVariable String songName) throws IOException {
        return new ResponseEntity<>(new ByteArrayResource(musicService.get(songName).getData()), HttpStatus.OK);
    }

    @PostMapping(value = "/{songName}", consumes = "multipart/form-data")
    public ResponseEntity<String> addSong(
            @PathVariable String songName,
            @RequestParam("file") MultipartFile file) throws IOException {

        musicService.save(file, songName);

        return new ResponseEntity<>(songName, HttpStatus.CREATED);
    }

    @DeleteMapping("/{songName}")
    public ResponseEntity<String> deleteSong(@PathVariable String songName) {

        musicService.delete(songName);

        return new ResponseEntity<>("Song deleted successfully", HttpStatus.OK);
    }
}
