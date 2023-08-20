package com.txt.kafka.exactlyonce.transaction;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class TransactionalMessageProducer {

    private static final String INPUT_TOPIC = "MES_trans_input";
    private static final String DATA_MESSAGE_1 = "Put any space separated data here for count";
    private static final String DATA_MESSAGE_2 = "Output will contain count of every word in the message";
    private static CountDownLatch latch = new CountDownLatch(3);

    public static void main(String[] args) {
        KafkaProducer<String, String> producer = createKafkaProducer();
        producer.initTransactions();
        try {
            producer.beginTransaction();
            Stream.of(DATA_MESSAGE_1, DATA_MESSAGE_2)
                    .forEach(s -> producer.send(new ProducerRecord<>(INPUT_TOPIC, null, s)));
            producer.commitTransaction();

            latch.await(10, TimeUnit.SECONDS);
        } catch (KafkaException | InterruptedException e) {
            producer.abortTransaction();
        }
    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(TRANSACTIONAL_ID_CONFIG, "prod-0");
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer(props);
    }
}
