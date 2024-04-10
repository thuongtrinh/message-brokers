package com.txt.flink;

import com.txt.flink.connector.Consumers;
import com.txt.flink.connector.Producers;
import com.txt.flink.operator.WordsCapitalizer;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkCapitalizeDataPipeline {

    public static void capitalize() throws Exception {
        String inputTopic = "flink_input";
        String outputTopic = "flink_output";
        String consumerGroup = "flink_group";
        String kafkaAddress = "localhost:9092";

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> kafkaSource = Consumers.createStringKafkaSource(inputTopic, kafkaAddress, consumerGroup);
        DataStream<String> dataStream = environment.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        KafkaSink<String> kafkaSink = Producers.createStringKafkaSink(outputTopic, kafkaAddress);

        DataStream<String> stringDataStream = dataStream.map(new WordsCapitalizer());

        // Add the sink to so results are written to the outputTopic
        stringDataStream.sinkTo(kafkaSink).name("Flink capitalize");

        environment.execute();
    }

    public static void main(String[] args) throws Exception {
        capitalize();
    }

}
