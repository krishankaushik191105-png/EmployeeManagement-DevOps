package com.example.employee.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents an Employee in the system.
 *
 * <p>This is a plain Java record used as the data model.
 * Validation annotations ensure incoming data is well-formed.</p>
 *
 * @param id         Unique identifier (assigned automatically by the service).
 * @param firstName  Employee's first name (required).
 * @param lastName   Employee's last name (required).
 * @param email      Employee's email address (required, must be valid format).
 * @param department Department the employee belongs to (required).
 * @param salary     Employee's salary (must be a positive number).
 */
public record Employee(
        Long id,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Department is required")
        String department,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be a positive number")
        Double salary
) {}
