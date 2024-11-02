package com.avr.mediastreamingserver.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.extern.slf4j.Slf4j;

import com.avr.mediastreamingserver.Constants.Constants;
import com.avr.mediastreamingserver.Model.DirectoryDiscoveryModel;
import com.avr.mediastreamingserver.Service.DirectoryDiscoveryInitialiser;
import com.avr.mediastreamingserver.Service.HashToMediaLocMap;
import com.avr.mediastreamingserver.Utils.Utils;



@Slf4j
@Controller
public class MediaController {

    private HashToMediaLocMap hashToMediaLocMap;
    private DirectoryDiscoveryInitialiser directoryDiscoveryInitialiser;

    public MediaController(HashToMediaLocMap hashToMediaLocMap, DirectoryDiscoveryInitialiser directoryDiscoveryInitialiser) {
        this.hashToMediaLocMap = hashToMediaLocMap;
        this.directoryDiscoveryInitialiser = directoryDiscoveryInitialiser;
    }
    
    @GetMapping("/")
    private String logRequestParams() {
        log.trace("Trace Message!");  
        log.debug("Debug Message!");  
        log.info("Info Message!");  
        log.warn("Warn Message!");  
        log.error("Error Message!");  
        return "hello";
    }

    @GetMapping("/stream/{fileHash}")
    public ResponseEntity<Resource> streamMediaFileRandomAccessFile(@PathVariable("fileHash") String fileHash, @RequestHeader HttpHeaders httpRequestHeaders) throws IOException {
        String fileName = hashToMediaLocMap.getFileLocFromHash(fileHash);
        Path filePath = Path.of(fileName).normalize();

        File mediaFile = filePath.toFile();
        long mediaFileLength = mediaFile.length();

        if(!mediaFile.exists()){
            log.error("File does not exist");
            throw new IOException("file does not exist");
        }

        List<HttpRange> httpRanges = httpRequestHeaders.getRange();
        
        if(httpRanges.isEmpty()) {
            Resource resource = new UrlResource(mediaFile.toURI());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Constants.EXTENSION_TO_CONTENT_TYPE_MAP.get(Utils.getFileExtension(fileName)))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(mediaFileLength))
                    .header(HttpHeaders.ACCEPT_RANGES, Constants.ACCEPTED_RANGE_BYTES)
                    .body(resource);
        }
        
        String[] rangeLimits = httpRanges.getFirst().toString().split("-");
        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath.toString(), "r");
        Long rangeStart = Long.parseLong(rangeLimits[0]);
        Long rangeEnd = rangeLimits.length > 1 ? Long.parseLong(rangeLimits[1]) : mediaFileLength - 1;
        Long rangeLength = rangeEnd - rangeStart + 1;

        randomAccessFile.seek(rangeStart);
        byte[] data = new byte[rangeLength.intValue()];

        randomAccessFile.readFully(data);
        randomAccessFile.close();

        Resource resource = new InputStreamResource(new ByteArrayInputStream(data));
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .header(HttpHeaders.CONTENT_TYPE, Constants.EXTENSION_TO_CONTENT_TYPE_MAP.get(fileName))
            .header(HttpHeaders.ACCEPT_RANGES, Constants.ACCEPTED_RANGE_BYTES)
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
            .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + mediaFileLength)
            .body(resource);
    }
    
    @GetMapping("/listMedia")
    public ResponseEntity<List<DirectoryDiscoveryModel>> listMediaEntity() throws UnsupportedEncodingException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON)
                .body(directoryDiscoveryInitialiser.getDiscoveredDirectories());
    }

}
