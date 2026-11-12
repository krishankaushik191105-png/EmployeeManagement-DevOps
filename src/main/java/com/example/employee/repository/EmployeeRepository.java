package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for storing and retrieving {@link Employee} records.
 *
 * <p>Uses a {@link ConcurrentHashMap} as the backing store so it is safe for
 * concurrent access. An {@link AtomicLong} acts as an auto-incrementing ID
 * generator — just like a database sequence.</p>
 *
 * <p>No database or JPA is needed: all data lives in memory and is reset
 * every time the application restarts. This is intentional for learning
 * purposes.</p>
 */
@Repository
public class EmployeeRepository {

    /** The in-memory store: key = employee ID, value = Employee record. */
    private final Map<Long, Employee> store = new ConcurrentHashMap<>();

    /** Auto-incrementing counter used to generate unique employee IDs. */
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Constructor: pre-loads some sample employees so the API has data
     * to demonstrate right out of the box.
     */
    public EmployeeRepository() {
        save(new Employee(null, "Alice",   "Johnson", "alice@example.com",   "Engineering", 85_000.0));
        save(new Employee(null, "Bob",     "Smith",   "bob@example.com",     "Marketing",   72_000.0));
        save(new Employee(null, "Charlie", "Brown",   "charlie@example.com", "Finance",     91_000.0));
    }

    /**
     * Returns all employees as an unmodifiable list.
     *
     * @return list of all employees (never null, may be empty).
     */
    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Finds a single employee by their ID.
     *
     * @param id The employee ID to search for.
     * @return an {@link Optional} containing the employee if found, or empty.
     */
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Saves a new employee to the in-memory store.
     * Automatically assigns a unique ID to the employee.
     *
     * @param employee The employee to save (id field is ignored / replaced).
     * @return The saved employee with the assigned ID.
     */
    public Employee save(Employee employee) {
        long newId = idCounter.getAndIncrement();
        // Records are immutable — create a new instance with the assigned ID
        Employee employeeWithId = new Employee(
                newId,
                employee.firstName(),
                employee.lastName(),
                employee.email(),
                employee.department(),
                employee.salary()
        );
        store.put(newId, employeeWithId);
        return employeeWithId;
    }

    /**
     * Returns the total number of employees currently stored.
     *
     * @return employee count.
     */
    public int count() {
        return store.size();
    }
}
