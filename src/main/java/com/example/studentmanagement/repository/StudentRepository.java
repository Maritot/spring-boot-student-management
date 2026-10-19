package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * REPOSITORY LAYER: StudentRepository
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is Spring Data JPA?
 *    It is an abstraction on top of JPA providers (Hibernate) that reduces
 *    boilerplate code by generating DAO implementation classes at runtime using
 *    dynamic proxies.
 *
 * 2. Repository Interface Hierarchy:
 *    - Repository<T, ID>: Marker interface, root of the hierarchy.
 *    - CrudRepository<T, ID>: Basic CRUD operations (save, findById, delete, etc.).
 *    - PagingAndSortingRepository<T, ID>: Adds pagination (Pageable) and sorting.
 *    - JpaRepository<T, ID>: Extends both, adds JPA-specific methods like
 *      flush(), saveAndFlush(), deleteInBatch(), returns List instead of Iterable.
 *
 * 3. Derived Query Methods (Query Creation from Method Names):
 *    Spring Data JPA parses method names like 'findByEmail' and generates SQL:
 *    "SELECT * FROM students WHERE email = ?"
 *
 * 4. Custom JPQL vs Native Query:
 *    - JPQL (Java Persistence Query Language): Operates on Entity class and fields
 *      (e.g., 'SELECT s FROM Student s WHERE s.course = :course'). DB-independent.
 *    - Native Query: Raw SQL with database-specific syntax (e.g., nativeQuery = true).
 *
 * 5. Why return Optional<T>?
 *    To prevent NullPointerException! Forces the caller to explicitly handle cases
 *    where a student record may not exist in the database (e.g., orElseThrow()).
 *
 * 6. Is @Repository annotation mandatory here?
 *    No! Spring Data automatically detects interfaces extending JpaRepository and
 *    creates bean implementations. However, adding @Repository is good documentation
 *    and enables Spring's DataAccessException translation mechanism.
 * ============================================================================
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Derived Query: Find single student by unique email
    Optional<Student> findByEmail(String email);

    // Derived Query: Check existence by email (very efficient: SELECT 1 FROM ...)
    boolean existsByEmail(String email);

    // Derived Query: Find list of students enrolled in a specific course
    List<Student> findByCourse(String course);

    // Derived Query: Find students older than a given age
    List<Student> findByAgeGreaterThan(Integer age);

    // Custom JPQL Query: Case-insensitive search on course name
    @Query("SELECT s FROM Student s WHERE LOWER(s.course) = LOWER(:course)")
    List<Student> searchByCourseIgnoreCase(@Param("course") String course);

    // Custom Native SQL Query example (for interview demonstration)
    @Query(value = "SELECT * FROM students WHERE email = :email", nativeQuery = true)
    Optional<Student> findByEmailNative(@Param("email") String email);
}
