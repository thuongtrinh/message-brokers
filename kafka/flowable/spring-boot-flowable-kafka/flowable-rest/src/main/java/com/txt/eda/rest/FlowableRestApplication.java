package com.txt.eda.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableKafka
@Slf4j
@SpringBootApplication
public class FlowableRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableRestApplication.class, args);
    }

}
