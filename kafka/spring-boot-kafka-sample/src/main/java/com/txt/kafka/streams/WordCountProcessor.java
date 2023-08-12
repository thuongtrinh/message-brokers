package com.txt.kafka.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WordCountProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Value(value = "${kafkastream.topic.in}")
    private String topicIn;

    @Value(value = "${kafkastream.topic.out}")
    private String topicOut;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
            .stream(topicIn, Consumed.with(STRING_SERDE, STRING_SERDE));

        KTable<String, Long> wordCounts = messageStream
            .mapValues((ValueMapper<String, String>) String::toLowerCase)
            .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
            .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
            .count(Materialized.as("counts"));

        wordCounts.toStream()
                .foreach((word, count) -> System.out.println("word: " + word + " -> " + count));

        wordCounts.toStream().to(topicOut);

        /*KTable<String, String> wordsTest = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .reduce(new Reducer<String>() {
                    @Override
                    public String apply(String k, String v) {
                        return k + "_" + v;
                    }
                });

        wordsTest.toStream().to(topicOut);*/
    }
}
