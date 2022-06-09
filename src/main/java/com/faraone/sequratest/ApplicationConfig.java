package com.faraone.sequratest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.faraone.sequratest.repository")
public class ApplicationConfig {

    @Autowired
    BaselineSetup baselineSetup;

    @PostConstruct
    public void init() {
        baselineSetup.init();
    }

}
