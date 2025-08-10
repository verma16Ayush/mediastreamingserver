package com.avr.mediastreamingserver.services;

import com.avr.mediastreamingserver.exceptions.FFmpegServiceException;
import com.avr.mediastreamingserver.exceptions.MediaScanningException;
import com.avr.mediastreamingserver.models.Video;
import com.avr.mediastreamingserver.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MediaScannerService {

    @Value("#{'${mediastreamingserver.root-media-path}'.split(',')}")
    List<String> mediaScanningRoots;

    @Autowired
    FFmpegService fFmpegService;

    @Autowired
    VideoService videoService;

    List<FFmpegServiceException> fFmpegServiceExceptions;

    public MediaScannerService() {
        fFmpegServiceExceptions = new ArrayList<>();
    }

    @Async("customThreadPoolExecutor")
    public void initialiseScanning() throws MediaScanningException {
        MediaScannerService.log.info("initialising scanning for media directories");
        for (String mediaScanningRoot : mediaScanningRoots) {
            log.info("scanning: {}", mediaScanningRoot);
            File root = new File(mediaScanningRoot);
            if (!root.exists()) continue;
            discoverAndAddVideosToDB(root, root);
            if(!fFmpegServiceExceptions.isEmpty()) {
                throw new MediaScanningException("errors occurred while scanning for media directories");
            }
        }
    }

    private void discoverAndAddVideosToDB(File scanningRoot, File thisLoc) {
        for(File file : Objects.requireNonNull(thisLoc.listFiles())) {
            if(file.isDirectory()) {
                if(Utils.directoryContainsFile(file, ".mss_ignore")) continue;
                discoverAndAddVideosToDB(scanningRoot, file);
            } else {
                if(!Utils.isValidVideoFile(file)) continue;
                try {
                    var fileHash = Utils.getPartialHashWithFileSize(file, 10);

                    if(!videoService.findById(fileHash).isEmpty())
                        continue;

                    fFmpegService.convertAndReplaceFile(file.getAbsolutePath());
                    Video video = fFmpegService.getVideoObjectFromFilePath(scanningRoot.getAbsolutePath(), file.getAbsolutePath(), fileHash);
                    log.info("scanned: {}", video.getTitle());
                    videoService.save(video);

                } catch (FFmpegServiceException | NoSuchAlgorithmException | IOException fe) {
                    log.error(fe.getMessage());
                    fFmpegServiceExceptions.add(new FFmpegServiceException(fe.getMessage(), fe.getCause()));
                }
            }
        }
    }

}
