package com.avr.mediastreamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MediastreamingserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediastreamingserverApplication.class, args);
	}

}
