package com.example.studentmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================================
 * SPRING CONFIGURATION CLASS: AppConfig
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. What is @Configuration?
 *    - Indicates that this class declares one or more @Bean methods and may be
 *      processed by the Spring container to generate bean definitions.
 *    - In Spring, @Configuration classes are proxied with CGLIB by default
 *      (proxyBeanMethods = true) to ensure singleton bean semantics.
 *
 * 2. @Bean vs @Component:
 *    - @Component: Class-level annotation used when YOU own the source code.
 *      Discovered via component scanning.
 *    - @Bean: Method-level annotation used when configuring third-party libraries
 *      (like ModelMapper, RestTemplate, WebClient) where you cannot modify the
 *      source code directly.
 *
 * 3. @Value Annotation:
 *    - Injects values from application.properties, environment variables, or JVM args.
 *    - Syntax: ${property.key:default_value} where the colon specifies a fallback value.
 * ============================================================================
 */
@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${app.welcome-message:Welcome to Spring Boot!}")
    private String welcomeMessage;

    @Value("${app.max-students-per-page:20}")
    private int maxStudentsPerPage;

    /**
     * Demonstrates declaring a custom Bean managed by Spring IoC container.
     */
    @Bean
    public AppPropertiesSummary appPropertiesSummary() {
        log.info("Creating AppPropertiesSummary bean with welcomeMessage: '{}'", welcomeMessage);
        return new AppPropertiesSummary(welcomeMessage, maxStudentsPerPage);
    }

    /**
     * Inner helper class representing configured app properties.
     */
    public static class AppPropertiesSummary {
        private final String welcomeMessage;
        private final int maxStudentsPerPage;

        public AppPropertiesSummary(String welcomeMessage, int maxStudentsPerPage) {
            this.welcomeMessage = welcomeMessage;
            this.maxStudentsPerPage = maxStudentsPerPage;
        }

        public String getWelcomeMessage() {
            return welcomeMessage;
        }

        public int getMaxStudentsPerPage() {
            return maxStudentsPerPage;
        }
    }
}
