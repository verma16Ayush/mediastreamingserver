package com.avr.mediastreamingserver.Utils;

import com.avr.mediastreamingserver.Constants.Constants;

public class Utils {

    public static boolean isValidVideoFile(String filePath) {
        String extension = getFileExtension(filePath);
        return extension.isEmpty() ? Boolean.FALSE : Constants.ACCPETED_VIDEO_FILE_FORMATS.contains(extension);
    }

    public static String getFileExtension(String filePath) {
        int indexOfExt = filePath.lastIndexOf(".");
        if(indexOfExt > 0) {
            return filePath.substring(indexOfExt + 1);
        }
        return "";
    } 
}
