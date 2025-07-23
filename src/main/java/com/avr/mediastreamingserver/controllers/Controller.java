package com.avr.mediastreamingserver.controllers;

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
    public Object healthCheck() throws IOException {
        var ffmpegVersion = fFmpegService.ensureFFmpegInstalled();
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse(InetAddress.getLocalHost().getHostAddress(), port, ffmpegVersion);
        healthCheckResponse.set
        return ResponseEntity.status(200).;
        return "the application is up and running on: " + InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }

}
