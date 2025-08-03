package com.avr.mediastreamingserver.exceptions;

public class VideoServiceException extends Exception {
    public VideoServiceException(String message) {
        super(message);
    }

    public VideoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
