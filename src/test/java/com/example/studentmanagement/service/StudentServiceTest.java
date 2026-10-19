package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.EmailAlreadyExistsException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * UNIT TEST: StudentServiceTest
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. Unit Test vs Integration Test:
 *    - Unit Test: Tests a single class in total ISOLATION without loading the
 *      Spring ApplicationContext or real database. Runs in milliseconds!
 *    - Integration Test (@SpringBootTest): Loads the full Spring context, real/in-memory
 *      DB, and tests interaction across layers.
 *
 * 2. Mockito Annotations:
 *    - @ExtendWith(MockitoExtension.class): Initializes Mockito mocks in JUnit 5.
 *    - @Mock: Creates a dummy/mock instance of StudentRepository. Does NOT hit DB!
 *    - @InjectMocks: Creates an instance of StudentServiceImpl and automatically
 *      injects the @Mock fields into it via constructor injection!
 *
 * 3. Testing Patterns (Arrange - Act - Assert / Given - When - Then):
 *    - Given / Arrange: Define mock behavior using when(mock.method()).thenReturn(val)
 *    - When / Act: Call the method under test.
 *    - Then / Assert: Verify result with assertions and mock interactions with verify().
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student sampleStudent;
    private StudentRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleStudent = new Student("John", "Doe", "john.doe@example.com", "Java Spring Boot", 25);
        sampleStudent.setId(1L);

        sampleRequest = new StudentRequest("John", "Doe", "john.doe@example.com", "Java Spring Boot", 25);
    }

    @Test
    @DisplayName("Should successfully create a student when email is unique")
    void createStudent_Success() {
        // Arrange
        when(studentRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(sampleStudent);

        // Act
        StudentResponse response = studentService.createStudent(sampleRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getFullName());
        assertEquals("john.doe@example.com", response.getEmail());

        // Verify that repository save was called exactly once
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void createStudent_ThrowsException_WhenEmailExists() {
        // Arrange
        when(studentRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            studentService.createStudent(sampleRequest);
        });

        // Verify save was NEVER called
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should return student when ID exists")
    void getStudentById_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(sampleStudent));

        // Act
        StudentResponse response = studentService.getStudentById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID not found")
    void getStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.getStudentById(99L);
        });
    }

    @Test
    @DisplayName("Should return all students")
    void getAllStudents_Success() {
        // Arrange
        Student student2 = new Student("Jane", "Doe", "jane.doe@example.com", "Python", 22);
        student2.setId(2L);
        when(studentRepository.findAll()).thenReturn(Arrays.asList(sampleStudent, student2));

        // Act
        List<StudentResponse> list = studentService.getAllStudents();

        // Assert
        assertEquals(2, list.size());
        assertEquals("John Doe", list.get(0).getFullName());
        assertEquals("Jane Doe", list.get(1).getFullName());
    }

    @Test
    @DisplayName("Should delete student when ID exists")
    void deleteStudent_Success() {
        // Arrange
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
