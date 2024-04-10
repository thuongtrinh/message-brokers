package com.txt.flink;

import com.txt.flink.connector.Consumers;
import com.txt.flink.connector.Producers;
import com.txt.flink.model.Backup;
import com.txt.flink.model.InputMessage;
import com.txt.flink.operator.BackupAggregator;
import com.txt.flink.operator.InputMessageTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import java.time.Duration;
import java.time.ZoneId;

public class FlinkCreateBackupDataPipeline {

    public static void createBackup() throws Exception {
        String inputTopic = "flink_input";
        String outputTopic = "flink_output";
        String consumerGroup = "flink_group";
        String kafkaAddress = "localhost:9092";

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<InputMessage> kafkaSource = Consumers.createInputMessageKafkaSource(inputTopic, kafkaAddress, consumerGroup);

        DataStream<InputMessage> dataStream = environment.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<InputMessage> backupDataStream = dataStream.assignTimestampsAndWatermarks(new InputMessageTimestampAssigner()
                .withTimestampAssigner((event, timestamp) -> event.getSentAt().atZone(ZoneId.of("UTC+7")).toEpochSecond()* 1000));

        KafkaSink<Backup> kafkaSink = Producers.createBackupKafkaSink(outputTopic, kafkaAddress);

        backupDataStream
                .windowAll(TumblingEventTimeWindows.of(Duration.ofHours(24)))
                .aggregate(new BackupAggregator())
                .sinkTo(kafkaSink)
                .name("Flink createBackup");

        environment.execute();
    }

    public static void main(String[] args) throws Exception {
        createBackup();
    }

}
