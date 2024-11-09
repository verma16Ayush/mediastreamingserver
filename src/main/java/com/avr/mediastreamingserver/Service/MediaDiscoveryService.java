package com.avr.mediastreamingserver.Service;

import org.springframework.stereotype.Service;

@Service
public class MediaDiscoveryService {
    // instead of traversing the directory again, I'm going to fetch it from the already created map
    // or i can walk the directories and not generate the hashes again, but seems pointless since the list of files should already be there
}
