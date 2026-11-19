package com.healthvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * HealthVault Cloud - Main Application Entry Point
 *
 * This is the starting point of the Spring Boot application.
 * @SpringBootApplication enables:
 *   - @Configuration: marks this as a config source
 *   - @EnableAutoConfiguration: auto-configures Spring based on dependencies
 *   - @ComponentScan: scans all sub-packages for Spring components
 */
@SpringBootApplication
public class HealthVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthVaultApplication.class, args);
        System.out.println("=========================================");
        System.out.println("  HealthVault Cloud is Running!");
        System.out.println("  Visit: http://localhost:8080");
        System.out.println("=========================================");
    }
}
