package com.example.employee.service;

import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Employee business logic.
 *
 * <p>The service sits between the controller (HTTP layer) and the repository
 * (data layer). It is responsible for:</p>
 * <ul>
 *   <li>Delegating CRUD operations to the repository.</li>
 *   <li>Throwing meaningful exceptions when business rules are violated
 *       (e.g., employee not found).</li>
 * </ul>
 *
 * <p>Keeping this logic out of the controller makes the code easier to
 * test and maintain.</p>
 */
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    /**
     * Constructor injection — Spring automatically provides the repository bean.
     *
     * @param repository The employee repository.
     */
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all employees from the data store.
     *
     * @return a list of all employees (never null, may be empty).
     */
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    /**
     * Retrieves a single employee by their ID.
     *
     * @param id The ID of the employee to retrieve.
     * @return The employee with the matching ID.
     * @throws EmployeeNotFoundException if no employee exists with that ID.
     */
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    /**
     * Adds a new employee to the data store.
     *
     * @param employee The employee to add (ID is assigned automatically).
     * @return The saved employee with the generated ID.
     */
    public Employee addEmployee(Employee employee) {
        return repository.save(employee);
    }
}
