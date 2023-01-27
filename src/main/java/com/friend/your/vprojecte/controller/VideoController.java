package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/all")
    public ResponseEntity<Page<String>> getAllVideos(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(videoService.getAllNames(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{videoName}")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoName) throws IOException {
        return new ResponseEntity<>(new ByteArrayResource(videoService.get(videoName).getData()), HttpStatus.OK);
    }

    @PostMapping(value = "/{videoName}", consumes = "multipart/form-data")
    public ResponseEntity<String> addVideo(
            @PathVariable String videoName,
            @RequestParam("file")MultipartFile file) throws IOException {

        videoService.save(file, videoName);

        return new ResponseEntity<>(videoName, HttpStatus.CREATED);
    }

    @DeleteMapping("/{videoName}")
    public ResponseEntity<String> deleteVideo(@PathVariable String videoName) {

        videoService.delete(videoName);

        return new ResponseEntity<>("Video deleted successfully", HttpStatus.OK);
    }
}
