package com.avr.mediastreamingserver.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideosResponse {    
    String title;
    String hash;
    long duration;
    String thumbnailSrc;
    LocalDateTime uploadedAt;
}
