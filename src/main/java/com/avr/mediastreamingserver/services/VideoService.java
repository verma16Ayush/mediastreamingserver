package com.avr.mediastreamingserver.services;

import com.avr.mediastreamingserver.exceptions.VideoServiceException;
import com.avr.mediastreamingserver.models.Video;
import com.avr.mediastreamingserver.repositories.VideoRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.util.StringUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository _videoRepository) {
        this.videoRepository = _videoRepository;
    }

    public Video save(Video _video) {
        // if(!videoRepository.findById(_video.getHash()).isEmpty()) {
        //     return null;
        // }
        return this.videoRepository.save(_video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> findById(String id) {
        return videoRepository.findById(id);
    }

    public List<Video> findByTitle(String title) {
        return videoRepository.searchByTitle(title);
    }

    public Page<Video> getPaginatedVideos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findAll(pageable);
    }

    public String resolvePathFromHash(String fileHash) {
        var video = this.findById(fileHash);
        if(video.isEmpty()) return "";
        return video.get().getResourcePath();
    }

    public static Resource getChunkedVideoResource(File file, long rangeSt, long rangeEn) throws VideoServiceException {

        if(!file.exists()) {
            throw new VideoServiceException("file doesnt exist: " + file.getAbsolutePath());
        }

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(rangeSt);
            long rangeLen = rangeEn - rangeSt + 1;
            InputStreamResource resource = new InputStreamResource(new InputStream() {
                private long remaining = rangeLen;
    
                @Override
                public int read() throws IOException {
                    if(remaining <= 0) return -1;
                    remaining--;
                    return raf.read();
                }
    
                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    if(remaining <= 0) return -1;
                    len = (int) Math.min(len, remaining);
                    int bytesRead = raf.read(b, off, len);
                    if(bytesRead >= 1) remaining -= bytesRead;
                    return bytesRead;
                }
    
                @Override
                public void close() throws IOException {
                    raf.close();
                }
            });
            return resource;
        } catch(IOException exception) {
            log.error(exception.getMessage());
            throw new VideoServiceException(String.format("error occurred while seeking file: %s, %d, %d", file.getName(), rangeSt, rangeEn), exception);
        }
        
    }
}
