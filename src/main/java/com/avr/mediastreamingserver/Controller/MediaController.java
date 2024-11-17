package com.avr.mediastreamingserver.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.avr.mediastreamingserver.Model.FileLocAndHashRecord;
import com.avr.mediastreamingserver.Service.DirectoryDiscoveryInitialiser;
import com.avr.mediastreamingserver.Service.MediaStore;
import com.avr.mediastreamingserver.Utils.Utils;




@Slf4j
@Controller
public class MediaController {

    private MediaStore mediaStore;
    private DirectoryDiscoveryInitialiser directoryDiscoveryInitialiser;

    public MediaController(MediaStore mediaStore, DirectoryDiscoveryInitialiser directoryDiscoveryInitialiser) {
        this.mediaStore = mediaStore;
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
        String fileName = mediaStore.getFileLocFromHash(fileHash);
        Path filePath = Path.of(fileName).normalize();

        File mediaFile = filePath.toFile();
        long mediaFileLength = mediaFile.length();

        if (!mediaFile.exists()) {
            throw new IOException("File does not exist");
        }

        List<HttpRange> httpRanges = httpRequestHeaders.getRange();

        if (httpRanges.isEmpty()) {
            Resource resource = new UrlResource(mediaFile.toURI());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_VIDEO_MP4)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(mediaFileLength))
                    .header(HttpHeaders.ACCEPT_RANGES, Constants.ACCEPTED_RANGE_BYTES)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);
        }

        HttpRange range = httpRanges.get(0);
        long rangeStart = range.getRangeStart(mediaFileLength);
        long rangeEnd = range.getRangeEnd(mediaFileLength);
        long rangeLength = rangeEnd - rangeStart + 1;

        RandomAccessFile randomAccessFile = new RandomAccessFile(mediaFile, "r");
        randomAccessFile.seek(rangeStart);

        InputStreamResource resource = new InputStreamResource(new InputStream() {
            private long remaining = rangeLength;

            @Override
            public int read() throws IOException {
                if (remaining <= 0) return -1;
                remaining--;
                return randomAccessFile.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (remaining <= 0) return -1;
                len = (int) Math.min(len, remaining);
                int bytesRead = randomAccessFile.read(b, off, len);
                if (bytesRead > 0) remaining -= bytesRead;
                return bytesRead;
            }

            @Override
            public void close() throws IOException {
                randomAccessFile.close();
            }
        });

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_VIDEO_MP4)
                .header(HttpHeaders.ACCEPT_RANGES, Constants.ACCEPTED_RANGE_BYTES)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + mediaFileLength)
                .body(resource);
    }

    @GetMapping("/listMediaWithDirectory")
    public ResponseEntity<List<DirectoryDiscoveryModel>> listMediaWithDirectory() throws UnsupportedEncodingException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON)
                .body(directoryDiscoveryInitialiser.getDiscoveredDirectories());
    }

    @GetMapping("/listMedia")
    public ResponseEntity<List<FileLocAndHashRecord>> getMethodName() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON)
                .body(mediaStore.getFileLocAndHashRecords());
    }
    
}
