package com.avr.mediastreamingserver.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class Controller {

    @Value("${server.port}")
    private int port;

    @GetMapping("/healthCheck")
    public String healthCheck() throws UnknownHostException {
        return "the application is up and running on: " + InetAddress.getLocalHost().getHostAddress() + ":" + port;
    }

}
