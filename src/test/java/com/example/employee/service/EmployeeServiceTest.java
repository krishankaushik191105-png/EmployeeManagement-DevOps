package com.example.employee.service;

import com.example.employee.exception.DepartmentNotFoundException;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmployeeService}.
 *
 * <p>Uses Mockito to mock the {@link EmployeeRepository} so the service
 * logic is tested in complete isolation — no Spring context, no I/O.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee(1L, "Alice", "Johnson",
                "alice@example.com", "Engineering", 85_000.0);
    }

    // -------------------------------------------------------------------------
    // getAllEmployees()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getAllEmployees() should return all employees from the repository")
    void getAllEmployees_shouldReturnAllEmployees() {
        when(repository.findAll()).thenReturn(List.of(sampleEmployee));

        List<Employee> result = service.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo("Alice");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllEmployees() should return an empty list when no employees exist")
    void getAllEmployees_shouldReturnEmptyListWhenNoEmployees() {
        when(repository.findAll()).thenReturn(List.of());

        List<Employee> result = service.getAllEmployees();

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // getEmployeeById()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getEmployeeById() should return the employee for a valid ID")
    void getEmployeeById_shouldReturnEmployeeWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        Employee result = service.getEmployeeById(1L);

        assertThat(result).isEqualTo(sampleEmployee);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getEmployeeById() should throw EmployeeNotFoundException for a missing ID")
    void getEmployeeById_shouldThrowExceptionWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEmployeeById(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("999");

        verify(repository, times(1)).findById(999L);
    }

    // -------------------------------------------------------------------------
    // addEmployee()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("addEmployee() should delegate to the repository and return the saved employee")
    void addEmployee_shouldReturnSavedEmployee() {
        Employee input = new Employee(
                null, "Bob", "Smith", "bob@example.com", "Marketing", 72_000.0);
        Employee saved = new Employee(
                2L, "Bob", "Smith", "bob@example.com", "Marketing", 72_000.0);

        when(repository.save(any(Employee.class))).thenReturn(saved);

        Employee result = service.addEmployee(input);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.firstName()).isEqualTo("Bob");
        verify(repository, times(1)).save(input);
    }

    // -------------------------------------------------------------------------
    // getEmployeesByDepartment()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getEmployeesByDepartment() should return matching employees")
    void getEmployeesByDepartment_shouldReturnMatchingEmployees() {
        when(repository.findByDepartment("Engineering")).thenReturn(List.of(sampleEmployee));

        List<Employee> result = service.getEmployeesByDepartment("Engineering");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).department()).isEqualTo("Engineering");
        verify(repository, times(1)).findByDepartment("Engineering");
    }

    @Test
    @DisplayName("getEmployeesByDepartment() should delegate the exact string to the repository (case handled there)")
    void getEmployeesByDepartment_shouldPassDepartmentStringToRepository() {
        // The service delegates the raw string to the repository, which handles case-insensitivity
        when(repository.findByDepartment("engineering")).thenReturn(List.of(sampleEmployee));

        List<Employee> result = service.getEmployeesByDepartment("engineering");

        assertThat(result).isNotEmpty();
        verify(repository, times(1)).findByDepartment("engineering");
    }

    @Test
    @DisplayName("getEmployeesByDepartment() should throw DepartmentNotFoundException when no employees found")
    void getEmployeesByDepartment_shouldThrowExceptionWhenNoneFound() {
        when(repository.findByDepartment("Nonexistent")).thenReturn(List.of());

        assertThatThrownBy(() -> service.getEmployeesByDepartment("Nonexistent"))
                .isInstanceOf(DepartmentNotFoundException.class)
                .hasMessageContaining("Nonexistent");

        verify(repository, times(1)).findByDepartment("Nonexistent");
    }
}
