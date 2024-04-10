package com.txt.flinkstream;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.dynamic.metadata.KafkaMetadataService;
import org.apache.flink.connector.kafka.dynamic.metadata.KafkaStream;
import org.apache.flink.connector.kafka.dynamic.source.DynamicKafkaSource;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class WordCount {

    final static String inputTopic = "flink_wordcount_input-topic";
    final static String outputTopic = "flink_wordcount_output-topic";
    final static String jobTitle = "WordCount";

    public static void main(String[] args) throws Exception {
        final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";

        // Set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        KafkaSource<String> source = KafkaSource.<String>builder()
//                .setBootstrapServers(bootstrapServers)
//                .setTopics(inputTopic)
//                .setGroupId("flink_wordcount_group")
//                .setStartingOffsets(OffsetsInitializer.earliest())
//                .setValueOnlyDeserializer(new SimpleStringSchema())
//                .build();

        KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
                .setValueSerializationSchema(new SimpleStringSchema())
                .setTopic(outputTopic)
                .build();

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setRecordSerializer(serializer)
                .build();

//        DataStream<String> text = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        DataStream<String> dataStream = env.fromData("data 01", "data 02", "data 03", "data 04", "data 05");

        // Split up the lines in pairs (2-tuples) containing: (word,1)
        DataStream<String> counts = dataStream.flatMap(new Tokenizer())
                // Group by the tuple field "0" and sum up tuple field "1"
                .keyBy(value -> value.f0)
                .sum(1)
                .flatMap(new Reducer());

        // Add the sink to so results are written to the outputTopic
        counts.sinkTo(kafkaSink);

        // Execute program
        env.execute(jobTitle);
    }

    /**
     * Implements the string tokenizer that splits sentences into words as a user-defined
     * FlatMapFunction. The function takes a line (String) and splits it into multiple pairs in the
     * form of "(word,1)" ({@code Tuple2<String, Integer>}).
     */
    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // Normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // Emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }

    // Implements a simple reducer using FlatMap to
    // reduce the Tuple2 into a single string for writing to kafka topics
    public static final class Reducer implements FlatMapFunction<Tuple2<String, Integer>, String> {

        @Override
        public void flatMap(Tuple2<String, Integer> value, Collector<String> out) {
            // Convert the pairs to a string for easy writing to Kafka Topic
            String count = value.f0 + " " + value.f1;
            out.collect(count);
        }
    }
}
