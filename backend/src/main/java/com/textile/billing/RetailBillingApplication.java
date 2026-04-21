package com.textile.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MAIN APPLICATION CLASS
 * -----------------------
 * This is the entry point of the Spring Boot application.
 *
 * @SpringBootApplication combines:
 *   - @Configuration     → marks this as a config class
 *   - @EnableAutoConfiguration → Spring Boot auto-sets up beans
 *   - @ComponentScan    → scans all sub-packages for components
 */
@SpringBootApplication
public class RetailBillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailBillingApplication.class, args);
        System.out.println("✅ Retail Billing System is running at http://localhost:8080");
    }
}
