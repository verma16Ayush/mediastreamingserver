package com.avr.mediastreamingserver.services;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FFmpegService {
    public String ensureFFmpegInstalled() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-version"
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader bbr = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        List<String> versionOutput = new ArrayList<>();
        while((line = bbr.readLine()) != null) {
            System.out.println(line);
            versionOutput.add(line);
        }
        return CollectionUtils.isEmpty(versionOutput) ? "" : versionOutput.get(0);
    }
}
