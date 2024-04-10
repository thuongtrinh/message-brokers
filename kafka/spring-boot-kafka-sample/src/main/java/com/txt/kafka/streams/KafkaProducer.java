package com.txt.kafka.streams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${kafkastream.topic.in}")
    private String topicIn;

    private static volatile int dem = 0;

    public void sendMessage(String message) {
        /*ListenableFuture<SendResult<String, String>> futured = kafkaTemplate.send(topicIn, "keysp" + dem++, message);
        futured.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message sent to topic: {}", message);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to send message", ex);
            }
        });*/

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicIn, "keysp" + dem++, message);
        future.handle((result, ex) -> {
            if (result != null) {
                log.info("Message sent to topic: {}", message);
                return result;
            } else if (ex != null) {
                log.error("Failed to send message", ex.getMessage());
                throw new RuntimeException(ex);
            } else {
                throw new RuntimeException("weird");
            }
        });
    }
}
