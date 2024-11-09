package com.avr.mediastreamingserver.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avr.mediastreamingserver.Model.DirectoryDiscoveryModel;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DirectoryDiscoveryInitialiser {

    @Value("#{'${mediadiscoveryloc}'.split(',')}") 
    List<String> mediaDiscoveryLocations;
    List<DirectoryDiscoveryModel> directoryDiscoveryModel;

    @Autowired
    private DirectoryDiscoveryService directoryDiscoveryService;

    @PostConstruct
    public void Initialise() throws UnsupportedEncodingException {            
        directoryDiscoveryModel = new ArrayList<>();
        for(String loc : mediaDiscoveryLocations) {
            directoryDiscoveryModel.add(directoryDiscoveryService.discover(loc));
            log.info("Media files discovered in: " + loc);
        }
    }

    public List<DirectoryDiscoveryModel> getDiscoveredDirectories() {
        return this.directoryDiscoveryModel;
    }

}
