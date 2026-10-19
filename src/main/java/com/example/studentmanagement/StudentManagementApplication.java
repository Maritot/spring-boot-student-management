package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * MAIN APPLICATION ENTRY POINT: StudentManagementApplication
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What does @SpringBootApplication do?
 *    It is a meta-annotation that bundles THREE essential annotations:
 *
 *    a) @SpringBootConfiguration:
 *       - A specialized version of @Configuration.
 *       - Tells Spring that this class can contain @Bean definitions.
 *
 *    b) @EnableAutoConfiguration:
 *       - The magic behind Spring Boot!
 *       - Automatically configures beans (DataSource, DispatcherServlet, Jackson,
 *         Tomcat server, JPA EntityManager) based on the JAR libraries present
 *         on your classpath (e.g. spring-boot-starter-web -> starts Tomcat).
 *       - Works via @Conditional annotations (@ConditionalOnClass,
 *         @ConditionalOnMissingBean, @ConditionalOnProperty).
 *
 *    c) @ComponentScan:
 *       - Tells Spring to scan the current package ('com.example.studentmanagement')
 *         and ALL its sub-packages for @Component, @Service, @Repository,
 *         @Controller, @RestController, and @Configuration.
 *       - TRICKY INTERVIEW QUESTION: "Why is my @Service bean not found?"
 *         Answer: If the service is placed in a package OUTSIDE the root package
 *         (e.g., 'com.other.service'), @ComponentScan will NOT find it unless
 *         you explicitly specify @ComponentScan("com.other").
 *
 * 2. What happens inside SpringApplication.run()?
 *    Step 1: Creates the Environment (reads application.properties, system env).
 *    Step 2: Prints the Spring Boot banner to console.
 *    Step 3: Creates the ApplicationContext (IoC Container).
 *    Step 4: Starts the Embedded Servlet Container (Apache Tomcat on port 8080).
 *    Step 5: Instantiates, wires, and initializes all Spring Beans (IoC / DI).
 *    Step 6: Executes all CommandLineRunner and ApplicationRunner beans.
 *    Step 7: Application is ready and starts accepting HTTP requests!
 * ============================================================================
 */
@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

}
