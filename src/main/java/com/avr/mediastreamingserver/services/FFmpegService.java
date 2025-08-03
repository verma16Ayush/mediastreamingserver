package com.avr.mediastreamingserver.services;

import com.avr.mediastreamingserver.exceptions.FFmpegServiceException;
import com.avr.mediastreamingserver.models.Video;
import static com.avr.mediastreamingserver.utils.Utils.*;

import com.avr.mediastreamingserver.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FFmpegService {

    public String ensureFFmpegInstalled() throws FFmpegServiceException {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-version"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader bbr = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> versionOutput = new ArrayList<>();
            while((line = bbr.readLine()) != null) {
                System.out.println(line);
                versionOutput.add(line);
            }
            return CollectionUtils.isEmpty(versionOutput) ? "" : versionOutput.get(0);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FFmpegServiceException("looks like ffmpeg is not installed", e);
        }
    }

    private JsonNode getVideoMetaData(String videoPath) throws FFmpegServiceException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "quiet",
                "-print_format", "json",
                "-show_format",
                "-show_streams",
                videoPath
        );

        pb.redirectErrorStream(true);
        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FFmpegServiceException("error getting video metadata from ffprobe", e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(json);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FFmpegServiceException("error mapping video metadata output", e);
        }
    }

    private String generateThumbnail(String scanningRoot, String videoFilePath, String fileHash) throws FFmpegServiceException {
        File mediaRoot = new File(scanningRoot);
        File videoFile = new File(videoFilePath);
        if(!mediaRoot.exists() || !videoFile.exists())
            return null;
        String newDirPath = createDirIfNotExists(scanningRoot, "thumbnails");
        if(newDirPath.isEmpty()) {
            throw new FFmpegServiceException("could not generate thumbnail directory");
        }
        String thumbnailPath = newDirPath.concat("/").concat(fileHash).concat(".png");
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i",
                videoFilePath,
                "-ss",
                "00:20:01.000",
                "-vframes",
                "1",
                thumbnailPath
        );
        pb.redirectErrorStream(true);
        try{
            pb.start();
            return thumbnailPath;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FFmpegServiceException("error occurred while generating thumbnail with ffmpeg", e);
        }
    }

    public Video getVideoObjectFromFilePath(String scanningRoot, String videoFilePath) throws FFmpegServiceException {
        Video video = new Video();
        File videoFile = new File(videoFilePath);
        if(!videoFile.exists() || !isValidVideoFile(videoFile))
            return null;

        try {
            var fileHash = getPartialHashWithFileSize(videoFile, 10);
            JsonNode fileJsonNode = this.getVideoMetaData(videoFile.getAbsolutePath());
            String title = getTitleFromPath(videoFilePath);
            String fileName = getFileNameFromPath(videoFilePath);
            String thumbnailPath = this.generateThumbnail(scanningRoot, videoFilePath, fileHash);
            long duration = Optional.ofNullable(fileJsonNode)
                    .map(node -> node.get("streams"))
                    .filter(streams -> streams.isArray() && streams.size() > 0)
                    .map(streams -> streams.get(0))
                    .map(stream -> stream.get("tags"))
                    .map(tags -> tags.get("DURATION"))
                    .map(JsonNode::asText)
                    .map(Utils::getDurationInMillis)
                    .orElse(0L);

            video.setHash(fileHash);
            video.setTitle(title);
            video.setFileName(fileName);
            video.setDuration(duration);
            video.setThumbnailPath(thumbnailPath);
            video.setUploadedAt(LocalDateTime.now());
            video.setResourcePath(videoFilePath);
            return video;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FFmpegServiceException("error occurred in creating the video object for file: " + videoFilePath, e);
        }
    }

}
