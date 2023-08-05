package com.txt.kafka.spring.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaConsumerListener {

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload = null;

    @KafkaListener(topics = "${topic.name}", groupId = "group-spring-test")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("received payload= " + consumerRecord.toString());
        setPayload(consumerRecord.toString());
        latch.countDown();
        System.out.println(latch.getCount());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String getPayload() {
        return payload;
    }

    private void setPayload(String payload) {
        this.payload = payload;
    }

    @KafkaListener(topics = "${topic.name}", groupId = "group-spring-test")
    public void listen(String message) {
        System.out.println("Received Message in group - group-id: " + message);
    }
}
