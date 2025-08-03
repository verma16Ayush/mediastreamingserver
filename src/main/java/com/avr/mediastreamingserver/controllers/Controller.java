package com.avr.mediastreamingserver.controllers;

import com.avr.mediastreamingserver.exceptions.FFmpegServiceException;
import com.avr.mediastreamingserver.exceptions.VideoServiceException;
import com.avr.mediastreamingserver.responseTypes.HealthCheckResponse;
import com.avr.mediastreamingserver.services.FFmpegService;
import com.avr.mediastreamingserver.services.VideoService;
import com.avr.mediastreamingserver.models.Video;
import com.avr.mediastreamingserver.repositories.VideoRepository;
import static com.avr.mediastreamingserver.constants.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    FFmpegService fFmpegService;

    @Value("${server.port}")
    private int port;

    @Autowired
    VideoService videoService;

    @GetMapping("/healthCheck")
    public ResponseEntity<HealthCheckResponse> healthCheck() throws FFmpegServiceException, UnknownHostException {
        var ffmpegVersion = fFmpegService.ensureFFmpegInstalled();
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse();
        healthCheckResponse.setHostAddress(InetAddress.getLocalHost().getHostAddress());
        healthCheckResponse.setPort(String.valueOf(port));
        healthCheckResponse.setFfmpegVersion(ffmpegVersion);
        return ResponseEntity
                .status(200)
                .body(healthCheckResponse);
    }

    @GetMapping("/videos")
    public ResponseEntity<Page<Video>> getAllVideos(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(videoService.getPaginatedVideos(page, size));
    }

    @GetMapping("/stream")
    public ResponseEntity<?> getMethodName(
        @RequestParam(value = "fileHash") String fileHash,
        @RequestHeader HttpHeaders reqHeaders
        ) throws VideoServiceException, IOException {
        var videoPath = videoService.resolvePathFromHash(fileHash);
        var videoFile = new File(videoPath);

        if(videoPath.isEmpty() ||!videoFile.exists()) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "requested resource not found"));
        }

        
        var videoFileLen = videoFile.length();

        List<HttpRange> httpRanges = reqHeaders.getRange();
        if(CollectionUtils.isEmpty(httpRanges)) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(videoFile));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(videoFile.toPath()))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoFileLen))
                    .header(HttpHeaders.ACCEPT_RANGES, ACCEPTED_RANGE_BYTES)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);
        }

        HttpRange range = httpRanges.get(0);
        var rangeSt = range.getRangeStart(videoFileLen);
        var rangeEn = range.getRangeEnd(videoFileLen);
        var rangeLen = rangeEn - rangeSt + 1;

        var partResource = VideoService.getChunkedVideoResource(videoFile, rangeSt, rangeEn);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VIDEO_MP4)
                .header(HttpHeaders.ACCEPT_RANGES, ACCEPTED_RANGE_BYTES)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLen))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeSt + "-" + rangeEn + "/" + videoFileLen)
                .body(partResource);
    }

}
