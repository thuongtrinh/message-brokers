package com.txt.kafka.spring.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StartupListener implements ApplicationRunner {

    @Value(value = "${topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg) {
        kafkaTemplate.send(topicName, msg);
    }

    @KafkaListener(topics = "${topic.name}", groupId = "group-spring-test")
    public void listen(String message) {
        System.out.println("Received Message in group - group-id: " + message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 100; i++) {
            sendMessage("Now: " + new Date());
            Thread.sleep(5000);
        }
    }
}
