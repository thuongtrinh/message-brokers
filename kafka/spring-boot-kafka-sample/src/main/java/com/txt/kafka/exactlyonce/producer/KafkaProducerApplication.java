package com.txt.kafka.exactlyonce.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@SpringBootApplication
public class KafkaProducerApplication {

    private final String TOPIC_NAME = "MES_EO_Producer";

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaProducerApplication.class, args);

        KafkaProducer<String, String> producer = (KafkaProducer<String, String>) context.getBean("kafkaProducerStr");
        EOKafkaProducer kafkaProducer = new EOKafkaProducer(producer);

        givenKeyValue_whenSend_thenSendOnlyAfterFlush(kafkaProducer);
//        givenKeyValue_whenSend_thenReturnException(kafkaProducer);
//        givenKeyValue_whenSendWithTxn_thenSendOnlyOnTxnCommit(kafkaProducer);

        context.close();
    }

    private static void givenKeyValue_whenSend_thenSendOnlyAfterFlush(EOKafkaProducer kafkaProducer) {
        Future<RecordMetadata> record = kafkaProducer.send("data", "{\"site\" : \"smith\"}");
        System.out.println(record.isDone());
        kafkaProducer.flush();
        System.out.println(record.isDone());
    }

    private static void givenKeyValue_whenSend_thenReturnException(EOKafkaProducer kafkaProducer) {
        Future<RecordMetadata> record = kafkaProducer.send("data", "{\"site\" : \"smith_ex\"}");
        RuntimeException e = new RuntimeException("abc");
        try {
            System.out.println(record.get());
        } catch (ExecutionException | InterruptedException ex) {
            e.printStackTrace();
        }
        System.out.println(record.isDone());
    }

    private static void givenKeyValue_whenSendWithTxn_thenSendOnlyOnTxnCommit(EOKafkaProducer kafkaProducer) throws Exception {
        kafkaProducer.initTransaction();
        kafkaProducer.beginTransaction();
        Future<RecordMetadata> record = kafkaProducer.send("data", "{\"site\" : \"smith_Txn\"}");
        System.out.println(record.get());
        kafkaProducer.commitTransaction();
    }

}
