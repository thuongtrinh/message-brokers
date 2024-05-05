package com.txt.eda.docore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync
@EnableKafka
public class DoCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoCoreServiceApplication.class, args);
    }

}
