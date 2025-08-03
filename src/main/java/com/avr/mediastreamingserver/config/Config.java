package com.avr.mediastreamingserver.config;

import com.avr.mediastreamingserver.exceptions.MediaScanningException;
import com.avr.mediastreamingserver.services.MediaScannerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config {

    @Bean(name = "customThreadPoolExecutor")
    public Executor customThreadPoolExecutor( ){
        return new ThreadPoolExecutor(
                3,
                5,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5)
        );
    }
}
