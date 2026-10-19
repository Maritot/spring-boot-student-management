package com.example.studentmanagement.dto;

import jakarta.validation.constraints.*;

/**
 * ============================================================================
 * DATA TRANSFER OBJECT (DTO): StudentRequest
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is a DTO and why do we use it?
 *    - Separation of Concerns: Decouples internal database entities from external
 *      API contracts.
 *    - Security: Prevents "Over-Posting" or "Mass Assignment" attacks where a
 *      malicious client tries to send fields like 'id' or 'isAdmin'.
 *    - Prevents Circular References: Directly serializing JPA entities with
 *      bidirectional relationships causes infinite loops and StackOverflowError!
 *
 * 2. Jakarta Bean Validation Annotations:
 *    - @NotBlank: Checks that a String is neither null nor contains only whitespace.
 *      (Difference: @NotNull allows empty string ""; @NotEmpty allows whitespace "  ";
 *       @NotBlank requires at least one non-whitespace character).
 *    - @Email: Validates proper email format.
 *    - @Min / @Max: Enforces numeric boundary limits.
 *    - @Size: Checks length for Strings/Collections.
 * ============================================================================
 */
public class StudentRequest {

    @NotBlank(message = "First name is mandatory and cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory and cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be a valid email address format (e.g. user@example.com)")
    private String email;

    @NotBlank(message = "Course is mandatory")
    private String course;

    @NotNull(message = "Age is mandatory")
    @Min(value = 16, message = "Student must be at least 16 years old")
    @Max(value = 100, message = "Age cannot exceed 100")
    private Integer age;

    public StudentRequest() {
    }

    public StudentRequest(String firstName, String lastName, String email, String course, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.course = course;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
