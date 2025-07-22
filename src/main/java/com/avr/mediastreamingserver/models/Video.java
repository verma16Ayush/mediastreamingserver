package com.avr.mediastreamingserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String fileName;
    private Long duration;
    private String description;
    private String thumbnailPath;
    private LocalDateTime uploadedAt;

    public Video(long id, String title, String fileName, Long duration, String description, String thumbnailPath, LocalDateTime uploadedAt) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.duration = duration;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.uploadedAt = uploadedAt;
    }
}
