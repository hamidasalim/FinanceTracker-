package com.fintech.enterprise.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to register the Jackson Hibernate module.
 * This module allows Jackson to correctly handle lazy-loaded Hibernate entities (proxies)
 * during JSON serialization. This specifically prevents the 500 error caused by
 * trying to serialize the internal "ByteBuddyInterceptor" when a lazy field
 * (like 'submittedBy' in Expense) hasn't been initialized yet.
 *
 * NOTE: We use Hibernate6Module as you are on Spring Boot 3+ (Hibernate 6).
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernateModule() {
        // By default, this module handles proxies. If a lazy association is not loaded,
        // it will be skipped from the JSON output instead of causing an error.
        // If the association is EAGER (which we recommend for 'submittedBy'), it will be serialized.
        return new Hibernate6Module();
    }
}
