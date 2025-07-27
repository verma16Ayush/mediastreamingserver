package com.avr.mediastreamingserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Utils {
    public static boolean isValidVideoFile(File file) {
        String filePath = file.getPath();
        String[] videFileExtensions = {".mp4", ".avi", ".mov", ".mkv", ".flv", ".wmv", ".webm"};
        String lowerPath = filePath.toLowerCase();
        return Arrays.stream(videFileExtensions).anyMatch(
                lowerPath::endsWith
        );
    }

    public static String getSHA256Hash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[8192];
            int bytesCount = 0;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

        }
        byte[] hashedBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for(byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getTitleFromPath(String path) {
        String fileName = getFileNameFromPath(path);
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static long getDurationInMillis(String durationString) {
        // "DURATION": "01:47:33.959000000"
        String[] parts = durationString.split(":");
        if(parts.length != 3) {
            return 0;
        }
        long hours = Long.parseLong(parts[0], 10);
        long minutes = Long.parseLong(parts[1]);
        String[] secondParts = parts[2].split("\\.");
        long seconds = Long.parseLong(secondParts[0]);
        long milliSeconds = secondParts.length > 1 ? Long.parseLong(secondParts[1].substring(0, 3)) : 0;

        return (hours * 3600 + minutes * 60 + seconds) * 1000 + milliSeconds;
    }


    public static String createDirIfNotExists(String dirPath, String directoryName) {
        File parentDirectory = new File(dirPath);
        if(!parentDirectory.isDirectory())
            return "";
        String newDirPath = dirPath.concat("/").concat(directoryName);
        File newDir = new File(newDirPath);
        if(!newDir.exists()) {
            if(newDir.mkdir()) {
                return newDirPath;
            }
        }
        return newDirPath;
    }

}
