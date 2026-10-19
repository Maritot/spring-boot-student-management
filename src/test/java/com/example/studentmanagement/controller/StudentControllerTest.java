package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ============================================================================
 * CONTROLLER SLICE TEST: StudentControllerTest
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is a Slice Test (@WebMvcTest)?
 *    - Instead of loading the ENTIRE application context (which loads JPA, H2 DB,
 *      services, repositories, and takes longer), @WebMvcTest ONLY boots the web layer!
 *    - Only Controller, ControllerAdvice, Filters, and Jackson serializers are loaded.
 *    - The Service layer MUST be mocked.
 *
 * 2. What is MockMvc?
 *    - Main entry point for server-side Spring MVC test support.
 *    - Simulates HTTP requests (GET, POST, PUT, DELETE) and validates responses
 *      (status code, JSON body, headers) WITHOUT starting a real HTTP server.
 *
 * 3. @MockitoBean vs @MockBean (IMPORTANT INTERVIEW QUESTION!):
 *    - In Spring Boot 1, 2, and up to 3.3: Developers used @MockBean.
 *    - In Spring Boot 3.4+ and 4.x: @MockBean is deprecated in favor of
 *      Spring Framework's standardized @MockitoBean!
 *    - Interviewers often ask this to test if your knowledge is up-to-date with
 *      the latest Spring releases.
 * ============================================================================
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    // Modern Spring Boot 3.4+ / 4.x mock bean annotation
    @MockitoBean
    private StudentService studentService;

    @Test
    @DisplayName("GET /api/v1/students - Should return list of students with HTTP 200 OK")
    void getAllStudents_ShouldReturnOk() throws Exception {
        StudentResponse student1 = new StudentResponse(1L, "Tharun", "Avula", "Tharun Avula",
                "tharun@example.com", "Java Spring Boot", 22, LocalDateTime.now(), LocalDateTime.now());
        StudentResponse student2 = new StudentResponse(2L, "Alice", "Smith", "Alice Smith",
                "alice@example.com", "Cloud Computing", 24, LocalDateTime.now(), LocalDateTime.now());

        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Tharun")))
                .andExpect(jsonPath("$[1].firstName", is("Alice")));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("GET /api/v1/students/1 - Should return student with HTTP 200 OK")
    void getStudentById_ShouldReturnOk() throws Exception {
        StudentResponse student = new StudentResponse(1L, "Tharun", "Avula", "Tharun Avula",
                "tharun@example.com", "Java Spring Boot", 22, LocalDateTime.now(), LocalDateTime.now());

        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Tharun Avula")))
                .andExpect(jsonPath("$.email", is("tharun@example.com")));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/students/99 - When not found, should return HTTP 404 NOT FOUND")
    void getStudentById_NotFound_ShouldReturn404() throws Exception {
        when(studentService.getStudentById(99L))
                .thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(get("/api/v1/students/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Student not found with id : '99'")));

        verify(studentService, times(1)).getStudentById(99L);
    }

    @Test
    @DisplayName("POST /api/v1/students - Should create student and return HTTP 201 CREATED")
    void createStudent_ShouldReturnCreated() throws Exception {
        StudentRequest request = new StudentRequest("Bob", "Marley", "bob.marley@example.com", "DevOps", 28);
        StudentResponse response = new StudentResponse(3L, "Bob", "Marley", "Bob Marley",
                "bob.marley@example.com", "DevOps", 28, LocalDateTime.now(), LocalDateTime.now());

        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.email", is("bob.marley@example.com")));

        verify(studentService, times(1)).createStudent(any(StudentRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/students - When invalid payload, should return HTTP 400 BAD REQUEST with field errors")
    void createStudent_InvalidPayload_ShouldReturnBadRequest() throws Exception {
        // Invalid request: blank names, invalid email, age under 16
        StudentRequest invalidRequest = new StudentRequest("", "", "not-an-email", "", 12);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.email", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.firstName", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.age", notNullValue()));

        // Verify service was NEVER called due to validation interceptor
        verify(studentService, never()).createStudent(any());
    }

    @Test
    @DisplayName("DELETE /api/v1/students/1 - Should return HTTP 204 NO CONTENT")
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
