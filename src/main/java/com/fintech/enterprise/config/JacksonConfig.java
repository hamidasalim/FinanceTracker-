package com.fintech.enterprise.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
