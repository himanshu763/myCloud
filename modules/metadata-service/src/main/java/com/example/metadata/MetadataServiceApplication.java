package com.example.metadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class MetadataServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MetadataServiceApplication.class, args);
    }
}