package com.example.employee;

import com.example.employee.controller.EmployeeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test that verifies the full Spring application context loads successfully.
 *
 * <p>{@code @SpringBootTest} starts the complete application context, confirming
 * that all beans wire together without configuration errors.
 * This is the simplest and most important test: if the context fails to start,
 * none of the other tests matter.</p>
 */
@SpringBootTest
@DisplayName("Application Context Smoke Test")
class EmployeeManagementApplicationTest {

    @Autowired
    private EmployeeController employeeController;

    @Test
    @DisplayName("Spring context should load and inject EmployeeController")
    void contextLoads() {
        // If this test passes, the entire Spring context started without errors
        assertThat(employeeController).isNotNull();
    }
}
