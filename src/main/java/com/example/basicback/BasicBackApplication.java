package com.example.basicback;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.example.basicback", "com.example.basicback.disasterfetcher"})
public class BasicBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicBackApplication.class, args);
    }
}