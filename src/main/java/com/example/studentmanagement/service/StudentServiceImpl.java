package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.EmailAlreadyExistsException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * SERVICE IMPLEMENTATION: StudentServiceImpl
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is @Service?
 *    - A specialization of @Component used to mark the service layer where
 *      core business logic lives.
 *    - It makes this class a Spring-managed Bean eligible for Dependency Injection.
 *
 * 2. Constructor Injection vs Field Injection:
 *    - BEST PRACTICE: Use Constructor Injection!
 *    - Why?
 *      a) Immutability: Dependencies can be declared 'final'.
 *      b) Pure Java Testing: You can write unit tests with 'new StudentServiceImpl(mockRepo)'
 *         without needing reflection or starting a full Spring container.
 *      c) Prevents NullPointerExceptions: Objects cannot be created in an incomplete state.
 *      d) Since Spring 4.3, @Autowired is OPTIONAL on a single constructor!
 *
 * 3. @Transactional:
 *    - Declares database transaction boundaries using Spring AOP (Aspect-Oriented Programming).
 *    - If an unchecked exception (RuntimeException) is thrown, the transaction rolls back.
 *    - @Transactional(readOnly = true): Optimizes queries by skipping Hibernate dirty-checking.
 * ============================================================================
 */
@Service
public class StudentServiceImpl implements StudentService {

    // Final dependency ensures immutability (best practice)
    private final StudentRepository studentRepository;

    /**
     * Constructor Injection: Spring automatically injects the StudentRepository bean.
     * Note: @Autowired annotation is omitted because it is optional when there is only one constructor.
     */
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        // Business Rule: Email must be unique
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Student with email '" + request.getEmail() + "' already exists!");
        }

        // Convert DTO to JPA Entity
        Student student = new Student(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getCourse(),
                request.getAge()
        );

        // Save entity to database via repository
        Student savedStudent = studentRepository.save(student);

        // Return clean response DTO
        return StudentResponse.fromEntity(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        return StudentResponse.fromEntity(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByCourse(String course) {
        return studentRepository.searchByCourseIgnoreCase(course)
                .stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        // Find existing student or throw 404
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // If email changed, verify new email is not already used by another student
        if (!existingStudent.getEmail().equalsIgnoreCase(request.getEmail()) &&
                studentRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Student with email '" + request.getEmail() + "' already exists!");
        }

        // Update fields
        existingStudent.setFirstName(request.getFirstName());
        existingStudent.setLastName(request.getLastName());
        existingStudent.setEmail(request.getEmail());
        existingStudent.setCourse(request.getCourse());
        existingStudent.setAge(request.getAge());

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentResponse.fromEntity(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        // Verify existence before deleting
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", "id", id);
        }
        studentRepository.deleteById(id);
    }
}
