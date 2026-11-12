package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller that exposes the Employee Management API endpoints.
 *
 * <p>All endpoints are prefixed with {@code /employees}.
 * Input validation is enforced via {@code @Valid} and Bean Validation
 * annotations on the {@link Employee} model.</p>
 *
 * <p>Error responses are handled centrally by
 * {@link com.example.employee.exception.GlobalExceptionHandler}.</p>
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructor injection of the service layer.
     *
     * @param employeeService The employee service.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // -------------------------------------------------------------------------
    // GET /employees
    // -------------------------------------------------------------------------

    /**
     * Retrieves a list of all employees.
     *
     * <p>Example request:</p>
     * <pre>GET http://localhost:8080/employees</pre>
     *
     * @return 200 OK with the list of all employees (empty list if none exist).
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // -------------------------------------------------------------------------
    // GET /employees/{id}
    // -------------------------------------------------------------------------

    /**
     * Retrieves a single employee by their ID.
     *
     * <p>Example request:</p>
     * <pre>GET http://localhost:8080/employees/1</pre>
     *
     * @param id The employee ID from the URL path.
     * @return 200 OK with the employee, or 404 Not Found if no employee exists
     *         with that ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // -------------------------------------------------------------------------
    // POST /employees
    // -------------------------------------------------------------------------

    /**
     * Adds a new employee to the system.
     *
     * <p>Example request body:</p>
     * <pre>
     * {
     *   "firstName":  "Jane",
     *   "lastName":   "Doe",
     *   "email":      "jane.doe@example.com",
     *   "department": "HR",
     *   "salary":     75000.0
     * }
     * </pre>
     *
     * @param employee The employee data from the request body.
     *                 {@code @Valid} triggers Bean Validation; invalid input
     *                 returns 400 Bad Request automatically.
     * @return 201 Created with the saved employee (including the generated ID).
     */
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
        Employee saved = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
