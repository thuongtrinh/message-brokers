package com.txt.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class StockServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(StockServiceApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(StockServiceApplication.class, args);
        SpringApplication app = new SpringApplication(StockServiceApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running!\n\t"
                        + "Ports {}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), env.getProperty("server.port"), env.getActiveProfiles());
    }

}
