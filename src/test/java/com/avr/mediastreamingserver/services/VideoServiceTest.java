package com.avr.mediastreamingserver.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.avr.mediastreamingserver.exceptions.VideoServiceException;
import com.avr.mediastreamingserver.models.Video;
import com.avr.mediastreamingserver.repositories.VideoRepository;

class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Video video = new Video();
        when(videoRepository.save(video)).thenReturn(video);

        Video savedVideo = videoService.save(video);

        assertNotNull(savedVideo);
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void testGetAllVideos() {
        List<Video> videos = List.of(new Video(), new Video());
        when(videoRepository.findAll()).thenReturn(videos);

        List<Video> result = videoService.getAllVideos();

        assertEquals(2, result.size());
        verify(videoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        String id = "123";
        Video video = new Video();
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));

        Optional<Video> result = videoService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(video, result.get());
        verify(videoRepository, times(1)).findById(id);
    }

    @Test
    void testFindByTitle() {
        String title = "test";
        List<Video> videos = List.of(new Video());
        when(videoRepository.searchByTitle(title)).thenReturn(videos);

        List<Video> result = videoService.findByTitle(title);

        assertEquals(1, result.size());
        verify(videoRepository, times(1)).searchByTitle(title);
    }

    @Test
    void testGetPaginatedVideos() {
        int page = 0, size = 2;
        PageRequest pageable = PageRequest.of(page, size);
        Page<Video> videoPage = new PageImpl<>(List.of(new Video(), new Video()));
        when(videoRepository.findAll(pageable)).thenReturn(videoPage);

        Page<Video> result = videoService.getPaginatedVideos(page, size);

        assertEquals(2, result.getContent().size());
        verify(videoRepository, times(1)).findAll(pageable);
    }

    @Test
    void testResolvePathFromHash() {
        String hash = "123";
        Video video = new Video();
        video.setResourcePath("/path/to/video");
        when(videoRepository.findById(hash)).thenReturn(Optional.of(video));

        String result = videoService.resolvePathFromHash(hash);

        assertEquals("/path/to/video", result);
        verify(videoRepository, times(1)).findById(hash);
    }
}