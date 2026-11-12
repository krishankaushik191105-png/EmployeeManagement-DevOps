package com.example.employee.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response body returned by the API for all errors.
 *
 * <p>Using a consistent structure helps API consumers parse errors
 * programmatically without special-casing each endpoint.</p>
 *
 * @param timestamp The date-time when the error occurred.
 * @param status    The HTTP status code (e.g., 404, 400).
 * @param error     A short description of the HTTP status (e.g., "Not Found").
 * @param message   A human-readable explanation of the error.
 * @param path      The request URI path that triggered the error.
 * @param details   An optional list of field-level validation error messages.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    /**
     * Convenience constructor for simple errors without field-level details.
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, List.of());
    }

    /**
     * Constructor for validation errors that include per-field messages.
     */
    public ErrorResponse(int status, String error, String message, String path, List<String> details) {
        this(LocalDateTime.now(), status, error, message, path, details);
    }
}
