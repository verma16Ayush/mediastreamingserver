package com.avr.mediastreamingserver.config;

import com.avr.mediastreamingserver.exceptions.MediaScanningException;
import com.avr.mediastreamingserver.services.MediaScannerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializerConfig {
    @Autowired
    MediaScannerService mediaScannerService;

    @PostConstruct
    public void initScanning() throws MediaScanningException {
        mediaScannerService.initialiseScanning();
    }
}
