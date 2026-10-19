package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================================
 * REST CONTROLLER LAYER: StudentController
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. @RestController vs @Controller:
 *    - @Controller: Used in traditional MVC web apps. Returns HTML template names
 *      (Thymeleaf/JSP) to be resolved by a ViewResolver.
 *    - @RestController: Convenience annotation = @Controller + @ResponseBody.
 *      Returns raw domain data (JSON/XML). Spring uses Jackson HttpMessageConverter
 *      to serialize Java objects into JSON automatically.
 *
 * 2. @RequestMapping:
 *    - Maps web requests to classes or handler methods.
 *    - Best practice: Include API versioning in the path (e.g., /api/v1/students).
 *
 * 3. HTTP Methods & Idempotence:
 *    - GET: Retrieve data. Safe & Idempotent (calling it multiple times has same effect).
 *    - POST: Create new resource. NOT Idempotent (calling multiple times creates duplicates).
 *    - PUT: Replace/update existing resource. Idempotent.
 *    - DELETE: Remove resource. Idempotent.
 *
 * 4. Parameters Extraction:
 *    - @PathVariable: Extracts variables from URI path (e.g. /students/{id}).
 *    - @RequestParam: Extracts query string parameters (e.g. /students?course=Java).
 *    - @RequestBody: Deserializes the incoming JSON HTTP body into a Java DTO.
 *    - @Valid: Triggers Hibernate Validator against the DTO before entering the method.
 *
 * 5. ResponseEntity<T>:
 *    - Wrapper representing the entire HTTP response: Status code + Headers + Body.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor Injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * CREATE a new student
     * POST /api/v1/students
     * Returns HTTP 201 CREATED
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET all students
     * GET /api/v1/students
     * Returns HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * GET a student by ID
     * GET /api/v1/students/{id}
     * Returns HTTP 200 OK (or 404 from GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        StudentResponse student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * FILTER students by course name
     * GET /api/v1/students/search?course=Java
     * Demonstrates @RequestParam
     */
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourse(@RequestParam String course) {
        List<StudentResponse> students = studentService.getStudentsByCourse(course);
        return ResponseEntity.ok(students);
    }

    /**
     * UPDATE an existing student
     * PUT /api/v1/students/{id}
     * Returns HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse updatedStudent = studentService.updateStudent(id, request);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * DELETE a student
     * DELETE /api/v1/students/{id}
     * Returns HTTP 204 NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
