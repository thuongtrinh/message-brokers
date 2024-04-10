package com.txt.flink.connector;

import com.txt.flink.model.InputMessage;
import com.txt.flink.schema.InputMessageDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

public class Consumers {

    public static KafkaSource<String> createStringKafkaSource(String topic, String kafkaAddress, String kafkaGroup) {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(kafkaAddress)
                .setTopics(topic)
                .setGroupId(kafkaGroup)
//                .setStartingOffsets(OffsetsInitializer.earliest())
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        return source;
    }

    public static KafkaSource<InputMessage> createInputMessageKafkaSource(String topic, String kafkaAddress, String kafkaGroup) {
        KafkaSource<InputMessage> source = KafkaSource.<InputMessage>builder()
                .setBootstrapServers(kafkaAddress)
                .setTopics(topic)
                .setGroupId(kafkaGroup)
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new InputMessageDeserializationSchema())
                .build();

        return source;
    }

}
