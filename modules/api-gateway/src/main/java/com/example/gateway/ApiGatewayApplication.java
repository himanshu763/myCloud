package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("metadata_service", r -> r.path("/api/metadata/**")
                .uri("http://localhost:8081")) // Replace with actual metadata service URL
            .route("file_service", r -> r.path("/api/files/**")
                .uri("http://localhost:8082")) // Replace with actual file service URL
            // Add more routes as needed
            .build();
    }
}