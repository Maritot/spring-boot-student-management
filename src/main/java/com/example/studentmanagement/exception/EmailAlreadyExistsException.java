package com.example.studentmanagement.exception;

/**
 * Custom runtime exception thrown when attempting to register or update
 * a student with an email that is already taken by another student.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
