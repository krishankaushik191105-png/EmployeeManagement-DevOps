package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EmployeeRepository}.
 *
 * <p>These tests run without any Spring context (plain JUnit 5),
 * making them extremely fast — no server or container is started.</p>
 */
@DisplayName("EmployeeRepository Tests")
class EmployeeRepositoryTest {

    private EmployeeRepository repository;

    /**
     * Re-creates a fresh repository before each test so tests are independent.
     * The constructor pre-seeds 3 employees, so we start with count = 3.
     */
    @BeforeEach
    void setUp() {
        repository = new EmployeeRepository();
    }

    // -------------------------------------------------------------------------
    // findAll()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findAll() should return the 3 pre-seeded employees")
    void findAll_shouldReturnAllPreSeededEmployees() {
        List<Employee> employees = repository.findAll();

        assertThat(employees).hasSize(3);
    }

    @Test
    @DisplayName("findAll() should return a non-null list")
    void findAll_shouldNeverReturnNull() {
        assertThat(repository.findAll()).isNotNull();
    }

    // -------------------------------------------------------------------------
    // findById()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findById() should return an employee for a valid ID")
    void findById_shouldReturnEmployeeWhenIdExists() {
        // The first pre-seeded employee has ID 1
        Optional<Employee> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().firstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("findById() should return empty Optional for a non-existent ID")
    void findById_shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Employee> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // save()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("save() should assign a unique ID to the new employee")
    void save_shouldAssignUniqueId() {
        Employee newEmployee = new Employee(
                null, "Dave", "Wilson", "dave@example.com", "IT", 60_000.0);

        Employee saved = repository.save(newEmployee);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.id()).isPositive();
    }

    @Test
    @DisplayName("save() should persist the employee so findById() can retrieve it")
    void save_shouldPersistEmployeeSoItCanBeRetrieved() {
        Employee newEmployee = new Employee(
                null, "Eve", "Taylor", "eve@example.com", "Legal", 95_000.0);

        Employee saved = repository.save(newEmployee);
        Optional<Employee> retrieved = repository.findById(saved.id());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().firstName()).isEqualTo("Eve");
        assertThat(retrieved.get().email()).isEqualTo("eve@example.com");
    }

    @Test
    @DisplayName("save() should increment the total employee count")
    void save_shouldIncrementCount() {
        int initialCount = repository.count();

        repository.save(new Employee(null, "Frank", "Lee", "frank@example.com", "Ops", 55_000.0));

        assertThat(repository.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("save() should assign different IDs to two successive employees")
    void save_shouldAssignDifferentIdsToSuccessiveEmployees() {
        Employee emp1 = repository.save(
                new Employee(null, "G", "H", "gh@example.com", "A", 50_000.0));
        Employee emp2 = repository.save(
                new Employee(null, "I", "J", "ij@example.com", "B", 60_000.0));

        assertThat(emp1.id()).isNotEqualTo(emp2.id());
    }
}
