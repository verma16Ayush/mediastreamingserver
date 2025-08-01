package com.avr.mediastreamingserver.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String MEDIA_FOLDER_LOC = "/Users/avr/Projects/mediastreamingserver/src/main/resources/media"; 
    public static final long MAX_VIDEO_CHUNK_SIZE = 500000L;
    public static final String CONTENT_TYPE_VIDEO_MP4 = "video/mp4";
    public static final String CONTENT_TYPE_VIDEO_MKV = "video/mkv";
    public static final Map<String, String> EXTENSION_TO_CONTENT_TYPE_MAP = new HashMap<String,String>() {{
        // put("mp4", "video/mp4");
        // put("mkv", "video/matroska");
        // put("mkv", "video/webm");
    }};
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    public static final String ACCEPTED_RANGE_BYTES = "bytes";
    public static final List<String> ACCPETED_VIDEO_FILE_FORMATS = List.of("mkv", "mp4", "flv");
    public static final String HASH_ALGORITHM_MD5 = "MD5";
    public static final String ENCODING_TYPE_UTF8 = "UTF-8";
    
    public Constants() {
    }
}
