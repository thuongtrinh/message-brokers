package com.txt.kafka.spring;

import java.util.concurrent.TimeUnit;

import com.txt.kafka.dto.Greeting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);

        MessageProducer producer = context.getBean(MessageProducer.class);
        MessageListener listener = context.getBean(MessageListener.class);

        /*
         * Sending a Hello World message to topic 'MESSAGE_01'. Must be received by both listeners with group foo and bar
         * with containerFactory fooKafkaListenerContainerFactory and barKafkaListenerContainerFactory respectively.
         * It will also be received by the listener with headersKafkaListenerContainerFactory as container factory.
         */
        producer.sendMessage("Hello, World!");
        listener.latch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to a topic with 5 partitions, each message to a different partition. But as per
         * listener configuration, only the messages from partition 0 and 3 will be consumed.
         */
        for (int i = 0; i < 3; i++) {
            producer.sendMessageToPartition("Hello To Partitioned Topic! " + i, i);
        }
        listener.partitionLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'filtered' topic. As per listener
         * configuration,  all messages with char sequence 'World' will be discarded.
         */
        producer.sendMessageToFiltered("Hello You!");
        producer.sendMessageToFiltered("Hello World!");
        listener.filterLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'greeting' topic. This will send
         * and received a java object with the help of
         * greetingKafkaListenerContainerFactory.
         */
        producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
        listener.greetingLatch.await(10, TimeUnit.SECONDS);

        System.out.println("---Done---");
        context.close();
    }
}
