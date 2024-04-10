package com.txt.flink.connector;

import com.txt.flink.model.Backup;
import com.txt.flink.schema.BackupSerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

public class Producers {

    public static KafkaSink<String> createStringKafkaSink(String outputTopic, String kafkaAddress) {
        KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new SimpleStringSchema())
                .setTopic(outputTopic)
                .build();

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(kafkaAddress)
                .setRecordSerializer(serializer)
                .build();
        return kafkaSink;
    }

    public static KafkaSink<Backup> createBackupKafkaSink(String outputTopic, String kafkaAddress) {
        KafkaRecordSerializationSchema<Backup> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new BackupSerializationSchema())
                .setTopic(outputTopic)
                .build();

        KafkaSink<Backup> kafkaSink = KafkaSink.<Backup>builder()
                .setBootstrapServers(kafkaAddress)
                .setRecordSerializer(serializer)
                .build();
        return kafkaSink;
    }

}
