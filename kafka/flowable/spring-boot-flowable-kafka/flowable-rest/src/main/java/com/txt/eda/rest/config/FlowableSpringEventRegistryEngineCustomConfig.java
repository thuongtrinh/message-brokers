package com.txt.eda.rest.config;


import org.flowable.eventregistry.spring.SpringEventRegistryEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FlowableSpringEventRegistryEngineCustomConfig {

    @Bean
    public SpringEventRegistryEngineConfiguration eventEngineConfiguration() {
        CustomSpringEventRegistryEngineConfiguration configuration = new CustomSpringEventRegistryEngineConfiguration();
        return configuration;
    }

}
