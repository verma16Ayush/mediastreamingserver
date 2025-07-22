package com.avr.mediastreamingserver.services;

import com.avr.mediastreamingserver.models.Video;
import com.avr.mediastreamingserver.repositories.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository _videoRepository) {
        this.videoRepository = _videoRepository;
    }

    public Video save(Video _video) {
        return this.videoRepository.save(_video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    public List<Video> findByTitle(String title) {
        return videoRepository.searchByTitle(title);
    }

}
