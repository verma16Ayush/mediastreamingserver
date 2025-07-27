package com.avr.mediastreamingserver.exceptions;

public class FFmpegServiceException extends Exception{
    public FFmpegServiceException(String message) {
        super(message);
    }

    public FFmpegServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
