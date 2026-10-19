package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.Student;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * DATA TRANSFER OBJECT (DTO): StudentResponse
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. Clean API Contract:
 *    The API client only receives the fields we explicitly expose.
 * 2. Static Factory Method Pattern (fromEntity):
 *    Centralizes the mapping logic from Entity -> DTO. In larger projects,
 *    libraries like MapStruct or ModelMapper are commonly used.
 * ============================================================================
 */
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String course;
    private Integer age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentResponse() {
    }

    public StudentResponse(Long id, String firstName, String lastName, String fullName,
                           String email, String course, Integer age,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.email = email;
        this.course = course;
        this.age = age;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory method to convert a Student JPA Entity into a StudentResponse DTO.
     */
    public static StudentResponse fromEntity(Student student) {
        if (student == null) {
            return null;
        }
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getFirstName() + " " + student.getLastName(),
                student.getEmail(),
                student.getCourse(),
                student.getAge(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
