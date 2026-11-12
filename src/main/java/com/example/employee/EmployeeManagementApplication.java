package com.example.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Employee Management REST API application.
 *
 * <p>This application demonstrates a beginner-friendly Spring Boot REST API
 * using an in-memory data store (no database required).</p>
 */
@SpringBootApplication
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }
}
