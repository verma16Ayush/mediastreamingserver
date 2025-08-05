package com.avr.mediastreamingserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {
    private String hostAddress;
    private String port;
    private String ffmpegVersion;
    private String lanAddress;
}
