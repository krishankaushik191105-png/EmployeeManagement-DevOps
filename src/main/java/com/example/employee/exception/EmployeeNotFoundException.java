package com.example.employee.exception;

/**
 * Exception thrown when an employee with a given ID is not found.
 *
 * <p>Extends {@link RuntimeException} so it does not need to be declared
 * in method signatures (unchecked exception).</p>
 */
public class EmployeeNotFoundException extends RuntimeException {

    private final Long employeeId;

    /**
     * Constructs the exception with a descriptive message.
     *
     * @param id The ID of the employee that was not found.
     */
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
        this.employeeId = id;
    }

    /**
     * Returns the ID of the employee that was not found.
     *
     * @return the employee ID.
     */
    public Long getEmployeeId() {
        return employeeId;
    }
}
