package com.txt.eda.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableKafka
@Slf4j
@SpringBootApplication
public class FlowableRestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FlowableRestApplication.class);
        Environment env = app.run(args).getEnvironment();

        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running!\n\t"
                        + "Port: {}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), env.getProperty("server.port"), env.getActiveProfiles());
    }

}
