package com.avr.mediastreamingserver.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    void testCreateDirIfNotExists() {
        File dirPath = new File("testDir");
        dirPath.mkdir(); // Ensure dirPath already exists
        String directoryName = "subDir";
        String result = Utils.createDirIfNotExists(dirPath.getPath(), directoryName);
        assertTrue(new File(result).exists());
        new File(result).delete(); // Cleanup
        dirPath.delete(); // Cleanup
    }

    @Test
    void testDirectoryContainsFile() {
        File directory = new File("testDir");
        directory.mkdir();
        File file = new File(directory, "testFile.txt");
        try {
            file.createNewFile();
            assertTrue(Utils.directoryContainsFile(directory, "testFile.txt"));
        } catch (IOException e) {
            fail("Failed to create test file");
        } finally {
            file.delete();
            directory.delete(); // Cleanup
        }
    }

    @Test
    void testGetDurationInMillis() {
        String durationString = "01:47:33.959";
        long duration = Utils.getDurationInMillis(durationString);
        assertEquals(6453959, duration);
    }

    @Test
    void testGetFileNameFromPath() {
        String path = "/path/to/file/video.mp4";
        String fileName = Utils.getFileNameFromPath(path);
        assertEquals("video.mp4", fileName);
    }

    @Test
    void testGetPartialHashWithFileSize() {
        File file = new File("testFile.txt");
        try {
            if (file.createNewFile()) {
                String hash = Utils.getPartialHashWithFileSize(file, 1);
                assertNotNull(hash);
                assertFalse(hash.isEmpty());
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            file.delete(); // Cleanup
        }
    }

    @Test
    void testGetTitleFromPath() {
        String path = "/path/to/file/video.mp4";
        String title = Utils.getTitleFromPath(path);
        assertEquals("video", title);
    }

    @Test
    void testIsValidVideoFile() {
        File validFile = new File("video.mp4");
        File invalidFile = new File("document.txt");
        assertTrue(Utils.isValidVideoFile(validFile));
        assertFalse(Utils.isValidVideoFile(invalidFile));
    }
}
