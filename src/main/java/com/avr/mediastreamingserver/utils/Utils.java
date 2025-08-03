package com.avr.mediastreamingserver.utils;

import org.aspectj.bridge.Message;

import static com.avr.mediastreamingserver.constants.Constants.ACCPETED_VIDEO_FILE_FORMATS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static boolean isValidVideoFile(File file) {
        String filePath = file.getPath();
        String lowerPath = filePath.toLowerCase();
        return ACCPETED_VIDEO_FILE_FORMATS.stream().anyMatch(
                lowerPath::endsWith
        );
    }


    public static String getPartialHashWithFileSize(File file, int size) throws NoSuchAlgorithmException, IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            final int HASH_CHUNK_SIZE = size * 1024 * 1024;
            StringBuilder sb = new StringBuilder();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            int totalRead = 0;
            while ((bytesRead = fis.read(buffer)) != -1 && totalRead < HASH_CHUNK_SIZE) {
                int actualBytesToHash = Math.min(bytesRead, HASH_CHUNK_SIZE - totalRead);
                messageDigest.update(buffer, 0, actualBytesToHash);
                totalRead += actualBytesToHash;
                if (totalRead >= HASH_CHUNK_SIZE)
                    break;
            }
            ByteBuffer fileSizeByteBuffer = ByteBuffer.allocate(Long.BYTES);
            fileSizeByteBuffer.putLong(file.length());
            messageDigest.update(fileSizeByteBuffer.array());

            byte[] finalHash = messageDigest.digest();
            for(byte b : finalHash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    public static String getFileNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static String getTitleFromPath(String path) {
        String fileName = getFileNameFromPath(path);
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static long getDurationInMillis(String durationString) {
        // "DURATION": ""01:47:33.959000000""
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

    public static boolean directoryContainsFile(File directory, String fileNameToFind){
        if(!directory.exists() || !directory.isDirectory()) {
            return false;
        }
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase(fileNameToFind);
            }
        };
        File[] matchingFiles = directory.listFiles(filter);
        return matchingFiles != null && matchingFiles.length > 0;
    }
}
