package com.faraone.sequratest;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.faraone.sequratest.repository")
public class ApplicationConfig {
}
