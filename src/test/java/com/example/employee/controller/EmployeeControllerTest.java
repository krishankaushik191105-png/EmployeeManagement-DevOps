package com.example.employee.controller;

import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration-style tests for {@link EmployeeController} using MockMvc.
 *
 * <p>{@code @WebMvcTest} spins up only the web layer (controller + filters),
 * NOT the full Spring context, so tests run fast.
 * The service is mocked with {@code @MockBean} to control behavior.</p>
 */
@WebMvcTest(EmployeeController.class)
@DisplayName("EmployeeController Tests")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee alice;
    private Employee bob;

    @BeforeEach
    void setUp() {
        alice = new Employee(1L, "Alice", "Johnson", "alice@example.com", "Engineering", 85_000.0);
        bob   = new Employee(2L, "Bob",   "Smith",   "bob@example.com",   "Marketing",  72_000.0);
    }

    // =========================================================================
    // GET /employees
    // =========================================================================

    @Test
    @DisplayName("GET /employees should return 200 and a list of all employees")
    void getAllEmployees_shouldReturn200WithEmployeeList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(alice, bob));

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[0].department", is("Engineering")))
                .andExpect(jsonPath("$[1].firstName", is("Bob")));
    }

    @Test
    @DisplayName("GET /employees should return 200 with an empty list when no employees exist")
    void getAllEmployees_shouldReturn200WithEmptyList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // =========================================================================
    // GET /employees/{id}
    // =========================================================================

    @Test
    @DisplayName("GET /employees/{id} should return 200 and the employee when found")
    void getEmployeeById_shouldReturn200WhenFound() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(alice);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",         is(1)))
                .andExpect(jsonPath("$.firstName",  is("Alice")))
                .andExpect(jsonPath("$.lastName",   is("Johnson")))
                .andExpect(jsonPath("$.email",      is("alice@example.com")))
                .andExpect(jsonPath("$.department", is("Engineering")))
                .andExpect(jsonPath("$.salary",     is(85_000.0)));
    }

    @Test
    @DisplayName("GET /employees/{id} should return 404 when the employee does not exist")
    void getEmployeeById_shouldReturn404WhenNotFound() throws Exception {
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status",  is(404)))
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    // =========================================================================
    // POST /employees
    // =========================================================================

    @Test
    @DisplayName("POST /employees should return 201 and the saved employee with an assigned ID")
    void addEmployee_shouldReturn201WithSavedEmployee() throws Exception {
        // Input has no ID (null); service returns one with an assigned ID
        Employee input = new Employee(null, "Charlie", "Brown",
                "charlie@example.com", "Finance", 91_000.0);
        Employee saved = new Employee(3L,   "Charlie", "Brown",
                "charlie@example.com", "Finance", 91_000.0);

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",        is(3)))
                .andExpect(jsonPath("$.firstName", is("Charlie")))
                .andExpect(jsonPath("$.email",     is("charlie@example.com")));
    }

    @Test
    @DisplayName("POST /employees should return 400 when firstName is blank")
    void addEmployee_shouldReturn400WhenFirstNameIsBlank() throws Exception {
        // firstName is blank — should fail validation
        String invalidJson = """
                {
                  "firstName":  "",
                  "lastName":   "Doe",
                  "email":      "jane.doe@example.com",
                  "department": "HR",
                  "salary":     75000.0
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",  is(400)))
                .andExpect(jsonPath("$.details", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("POST /employees should return 400 when email format is invalid")
    void addEmployee_shouldReturn400WhenEmailIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "firstName":  "Jane",
                  "lastName":   "Doe",
                  "email":      "not-an-email",
                  "department": "HR",
                  "salary":     75000.0
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.details[0]", containsString("email")));
    }

    @Test
    @DisplayName("POST /employees should return 400 when salary is negative")
    void addEmployee_shouldReturn400WhenSalaryIsNegative() throws Exception {
        String invalidJson = """
                {
                  "firstName":  "Jane",
                  "lastName":   "Doe",
                  "email":      "jane@example.com",
                  "department": "HR",
                  "salary":     -500.0
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("POST /employees should return 400 when request body is missing")
    void addEmployee_shouldReturn400WhenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
