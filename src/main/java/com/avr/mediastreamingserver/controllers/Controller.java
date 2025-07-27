package com.avr.mediastreamingserver.controllers;

import com.avr.mediastreamingserver.exceptions.FFmpegServiceException;
import com.avr.mediastreamingserver.responseTypes.HealthCheckResponse;
import com.avr.mediastreamingserver.services.FFmpegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class Controller {

    @Autowired
    FFmpegService fFmpegService;

    @Value("${server.port}")
    private int port;

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


}
