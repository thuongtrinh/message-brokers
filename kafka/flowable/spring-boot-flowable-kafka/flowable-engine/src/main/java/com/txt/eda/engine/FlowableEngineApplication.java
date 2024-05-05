package com.txt.eda.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableKafka
@Slf4j
public class FlowableEngineApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(FlowableEngineApplication.class);

        Environment env = app.run(args).getEnvironment();
        log.info("=================ENV============: {}", env.getProperty("spring.kafka.bootstrap-servers"));

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Protocol: \t\t{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getActiveProfiles());
    }

}
