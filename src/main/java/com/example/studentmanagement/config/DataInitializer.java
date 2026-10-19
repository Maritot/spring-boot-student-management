package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * DATA INITIALIZER: CommandLineRunner
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is CommandLineRunner / ApplicationRunner?
 *    - Spring Boot interfaces that provide a run() method.
 *    - Executes automatically AFTER the Spring ApplicationContext is fully loaded
 *      and bean creation is complete, but BEFORE the application starts accepting
 *      traffic.
 *    - Difference between CommandLineRunner and ApplicationRunner:
 *      * CommandLineRunner accepts raw varargs (String... args).
 *      * ApplicationRunner provides an ApplicationArguments wrapper with convenient
 *        methods to access parsed flags (--server.port, etc.).
 *
 * 2. Why use it?
 *    - Seeding dummy / reference data into database (e.g. initial admin user, roles).
 *    - Warming up caches or checking third-party service connections at startup.
 * ============================================================================
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final StudentRepository studentRepository;

    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Checking if database needs sample student seed data...");

        if (studentRepository.count() == 0) {
            List<Student> initialStudents = Arrays.asList(
                    new Student("Tharun", "Avula", "tharun.avula@example.com", "Java Spring Boot", 22),
                    new Student("Alice", "Johnson", "alice.johnson@example.com", "Cloud Computing", 24),
                    new Student("Bob", "Smith", "bob.smith@example.com", "Data Science", 21),
                    new Student("Charlie", "Brown", "charlie.brown@example.com", "Java Spring Boot", 23)
            );

            studentRepository.saveAll(initialStudents);
            log.info("Successfully seeded {} sample students into H2 in-memory database!", initialStudents.size());
        } else {
            log.info("Database already contains records. Skipping seed data.");
        }
    }
}
