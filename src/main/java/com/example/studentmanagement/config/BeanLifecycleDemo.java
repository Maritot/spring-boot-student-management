package com.example.studentmanagement.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 * BEAN LIFECYCLE DEMO: BeanLifecycleDemo
 * ============================================================================
 * INTERVIEW CONCEPTS COVERED:
 * 1. The 10 Stages of Spring Bean Lifecycle:
 *    Stage 1:  Bean Definition Loading (from @Component, @Bean, or XML)
 *    Stage 2:  Bean Instantiation (Constructor is invoked)
 *    Stage 3:  Populate Properties (Dependency Injection occurs)
 *    Stage 4:  Aware Interfaces (BeanNameAware, BeanFactoryAware, ApplicationContextAware)
 *    Stage 5:  BeanPostProcessor - postProcessBeforeInitialization()
 *    Stage 6:  @PostConstruct initialization callback
 *    Stage 7:  InitializingBean - afterPropertiesSet()
 *    Stage 8:  Custom init-method
 *    Stage 9:  BeanPostProcessor - postProcessAfterInitialization()
 *    --- BEAN IS READY TO SERVE REQUESTS ---
 *    Stage 10: Container Shutdown:
 *              a) @PreDestroy callback
 *              b) DisposableBean - destroy()
 *              c) Custom destroy-method
 *
 * 2. Why use @PostConstruct?
 *    Constructor should ONLY initialize basic member variables. If you need to
 *    fetch external configuration, connect to a cache, or validate injected dependencies,
 *    use @PostConstruct because all dependencies (@Autowired) are guaranteed to be
 *    injected before @PostConstruct runs!
 * ============================================================================
 */
@Component
public class BeanLifecycleDemo {

    private static final Logger log = LoggerFactory.getLogger(BeanLifecycleDemo.class);

    public BeanLifecycleDemo() {
        log.info("[BEAN LIFECYCLE 1/3] Constructor called: BeanLifecycleDemo is instantiated.");
    }

    @PostConstruct
    public void init() {
        log.info("[BEAN LIFECYCLE 2/3] @PostConstruct called: Dependencies are injected, Bean is ready for use.");
    }

    @PreDestroy
    public void cleanup() {
        log.info("[BEAN LIFECYCLE 3/3] @PreDestroy called: Application is shutting down, releasing resources.");
    }
}
