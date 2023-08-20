package com.txt.kafka.exactlyonce.serdes;

import com.txt.kafka.dto.MessageDto;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


@SpringBootApplication
public class KafkaSerdesApplication {

    private static String topicName = "MES_serdes";

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaSerdesApplication.class, args);

        System.out.println(topicName);

//        KafkaProducer<String, MessageDto> producer = context.getBean(KafkaProducer.class);
        KafkaProducer<String, MessageDto> producer = (KafkaProducer<String, MessageDto>) context.getBean("kafkaProducerMes");
        MessageDto msgProd = MessageDto.builder()
                .message("test")
                .version("1.0")
                .build();

        producer.send(new ProducerRecord<>(topicName, "1", msgProd));
        System.out.println("Message sent " + msgProd);
        producer.close();

        Thread.sleep(2000);

        AtomicReference<MessageDto> msgCons = new AtomicReference<>();

        KafkaConsumer<String, MessageDto> consumer = (KafkaConsumer<String, MessageDto>) context.getBean("kafkaConsumerMes");
        consumer.subscribe(Arrays.asList(topicName));

        ConsumerRecords<String, MessageDto> records = consumer.poll(Duration.ofSeconds(5));
        records.forEach(record -> {
            msgCons.set(record.value());
            System.out.println("Message received " + record.value());
        });

        System.out.println("msgCons: " + msgCons.get());
        consumer.close();

        context.close();
    }

}
