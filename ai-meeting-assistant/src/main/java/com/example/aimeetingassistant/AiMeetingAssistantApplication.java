package com.example.aimeetingassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Import this

@SpringBootApplication // Enables auto-configuration, component scanning, etc.
@EnableAsync // Add this annotation for potential future background tasks
public class AiMeetingAssistantApplication {

    public static void main(String[] args) {
        // Launches the Spring Boot application
        SpringApplication.run(AiMeetingAssistantApplication.class, args);
    }

}