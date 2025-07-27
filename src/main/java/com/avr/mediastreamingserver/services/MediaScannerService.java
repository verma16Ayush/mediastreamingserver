package com.avr.mediastreamingserver.services;

import com.avr.mediastreamingserver.exceptions.FFmpegServiceException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class MediaScannerService {

    @Value("#{'${mediastreamingserver.root-media-path}'.split(',')}")
    List<String> mediaScanningRoots;

    @Autowired
    FFmpegService fFmpegService;



    @PostConstruct
    public void initialiseScanning() throws FFmpegServiceException {
        MediaScannerService.log.info("initialising scanning for media directories");
        mediaScanningRoots.forEach(log::info);
        String filePath = mediaScanningRoots.get(0) + "/Lilo and Stich 2025 1080p REPACK WEB-DL HEVC x265 5.1 BONE.mkv";
        File file = new File(filePath);
        if(file.exists()) {
            System.out.println(fFmpegService.getVideoObjectFromFilePath(mediaScanningRoots.get(0), filePath));
        }
    }



}
