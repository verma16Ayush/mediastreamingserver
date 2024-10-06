package com.avr.mediastreamingserver.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class Controller {
    
    @GetMapping(path = "/")
    private String logRequestParams() {
        log.trace("Trace Message!");  
        log.debug("Debug Message!");  
        log.info("Info Message!");  
        log.warn("Warn Message!");  
        log.error("Error Message!");  
        return "hello";
    }
}
