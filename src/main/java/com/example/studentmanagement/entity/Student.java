package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * JPA ENTITY: Student
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is an Entity?
 *    An entity represents a table stored in a relational database. Every instance
 *    of an entity corresponds to a single row in that table.
 *
 * 2. @Entity vs @Table:
 *    - @Entity: Mandatory annotation from Jakarta Persistence. Tells Spring Boot
 *      and Hibernate that this class is a persistent domain object.
 *    - @Table: Optional. Specifies the database table name. If omitted, the table
 *      name defaults to the class name (Student).
 *
 * 3. Primary Key & Generation Strategies:
 *    - @Id: Specifies the primary key of an entity.
 *    - @GeneratedValue(strategy = GenerationType.IDENTITY): Uses the database's
 *      identity column (auto-increment in MySQL, PostgreSQL, H2).
 *    Other strategies:
 *      * SEQUENCE: Uses a DB sequence (best for Oracle, PostgreSQL).
 *      * TABLE: Simulates sequence using a dedicated DB table (slowest).
 *      * AUTO: JPA picks the strategy automatically based on the DB dialect.
 *
 * 4. Why is a No-Argument Constructor mandatory in JPA?
 *    Hibernate uses Java Reflection (Class.getDeclaredConstructor().newInstance())
 *    to instantiate entities when loading rows from the database. Without a
 *    no-arg constructor, Hibernate will throw an InstantiationException!
 *
 * 5. Lifecycle Callbacks (@PrePersist, @PreUpdate):
 *    Allows executing logic automatically before saving or updating a row.
 * ============================================================================
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "course", nullable = false, length = 100)
    private String course;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Default no-arg constructor required by JPA/Hibernate.
     */
    public Student() {
    }

    /**
     * Parameterized constructor for convenient object creation.
     */
    public Student(String firstName, String lastName, String email, String course, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.course = course;
        this.age = age;
    }

    // ------------------------------------------------------------------------
    // JPA Entity Lifecycle Callbacks
    // ------------------------------------------------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------------------------------------------------------
    // Getters and Setters
    // (JPA and Jackson use these to access and serialize properties)
    // ------------------------------------------------------------------------

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

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", age=" + age +
                '}';
    }
}
