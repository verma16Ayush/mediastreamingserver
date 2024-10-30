package com.avr.mediastreamingserver.Utils;

import com.avr.mediastreamingserver.Constants.Constants;

public class Utils {
    public static long getRangeEnd(long rangeStart, long maxChunkSize, long fileLength) {
        return rangeStart  + maxChunkSize >= fileLength ? fileLength - 1 : rangeStart + maxChunkSize;
    }

    public static boolean isValidVideoFile(String filePath) {
        int indexOfExt = filePath.lastIndexOf(".");
        if(indexOfExt > 0) {
            String extension = filePath.substring(indexOfExt + 1);
            return Constants.ACCPETED_VIDEO_FILE_FORMATS.contains(extension);
        }
        return Boolean.FALSE;
    }
}
