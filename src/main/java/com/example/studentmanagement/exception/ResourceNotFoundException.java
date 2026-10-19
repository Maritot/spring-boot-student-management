package com.example.studentmanagement.exception;

/**
 * Custom runtime exception thrown when a requested entity cannot be found.
 * In interviews: Unchecked exceptions (subclasses of RuntimeException) are
 * preferred in modern Spring applications because Spring's @Transactional
 * automatically rolls back on unchecked exceptions by default.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
