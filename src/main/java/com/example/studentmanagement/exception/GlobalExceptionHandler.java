package com.example.studentmanagement.exception;

import com.example.studentmanagement.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * GLOBAL EXCEPTION HANDLER
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is @RestControllerAdvice?
 *    - It is a specialization of @Component that acts as an interceptor around
 *      all @RestController classes across the application.
 *    - Combines @ControllerAdvice + @ResponseBody, meaning return values are
 *      automatically serialized to JSON!
 *
 * 2. How does @ExceptionHandler work?
 *    When any Controller method throws an exception matching the declared type,
 *    Spring catches it and routes it directly to this annotated method.
 *
 * 3. Handling Validation Errors (MethodArgumentNotValidException):
 *    When @Valid fails on a @RequestBody, Spring throws MethodArgumentNotValidException.
 *    We extract each FieldError to provide exact, readable field-by-field error messages.
 *
 * 4. Benefits in Production:
 *    - Never exposes raw database errors or stack traces to clients (Security!).
 *    - Consistent, structured JSON error schema across all endpoints.
 *    - Proper HTTP Status Codes (404, 400, 409, 500).
 * ============================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles 404 NOT FOUND when a requested student/resource does not exist.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles 409 CONFLICT when trying to register duplicate unique data (e.g. email).
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handles 400 BAD REQUEST when @Valid fails on request body DTOs.
     * Collects all field-level validation errors into a key-value map.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed for one or more fields",
                request.getRequestURI(),
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback handler for any uncaught 500 INTERNAL SERVER ERROR exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected internal error occurred: " + ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
