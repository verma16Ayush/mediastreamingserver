package com.avr.mediastreamingserver.Utils;

public class Utils {
    public static long getRangeEnd(long rangeStart, long maxChunkSize, long fileLength) {
        return rangeStart  + maxChunkSize >= fileLength ? fileLength - 1 : rangeStart + maxChunkSize;
    }
}
