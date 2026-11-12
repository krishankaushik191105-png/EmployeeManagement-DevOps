package com.example.employee.exception;

/**
 * Exception thrown when no employees are found for a given department name.
 *
 * <p>Extends {@link RuntimeException} so it does not need to be declared
 * in method signatures (unchecked exception).</p>
 */
public class DepartmentNotFoundException extends RuntimeException {

    private final String department;

    /**
     * Constructs the exception with a descriptive message.
     *
     * @param department The department name that yielded no results.
     */
    public DepartmentNotFoundException(String department) {
        super("No employees found in department: " + department);
        this.department = department;
    }

    /**
     * Returns the department name that was searched for.
     *
     * @return the department name.
     */
    public String getDepartment() {
        return department;
    }
}
