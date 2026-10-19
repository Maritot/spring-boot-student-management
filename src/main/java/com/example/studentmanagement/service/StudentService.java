package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;

import java.util.List;

/**
 * ============================================================================
 * SERVICE INTERFACE: StudentService
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. Interface-Driven Design (SOLID Principles):
 *    - Dependency Inversion Principle: High-level modules should depend on
 *      abstractions, not on concrete implementations.
 *    - Allows easy mocking in unit tests and interchangeable implementations.
 * ============================================================================
 */
public interface StudentService {

    StudentResponse createStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Long id);

    List<StudentResponse> getStudentsByCourse(String course);

    StudentResponse updateStudent(Long id, StudentRequest request);

    void deleteStudent(Long id);
}
