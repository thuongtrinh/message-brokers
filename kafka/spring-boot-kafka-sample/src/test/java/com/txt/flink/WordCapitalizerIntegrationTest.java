package com.txt.flink;

import com.txt.flink.model.InputMessage;
import com.txt.flink.operator.WordsCapitalizer;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordCapitalizerIntegrationTest {

    @Test
    public void givenDataSet_whenExecuteWordCapitalizer_thenReturnCapitalizedWords() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<String> data = Arrays.asList("dog", "cat", "wolf", "pig");

        DataStream<String> dataStream = env.fromData(data);

        DataStream<String> stringDataStream = dataStream.map(new WordsCapitalizer());


        List<String> dataProcessed = stringDataStream
                .map(new WordsCapitalizer())
                .executeAndCollect(data.size());

        List<String> testDataCapitalized = data.stream()
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        Assert.assertEquals(testDataCapitalized, dataProcessed.stream().sorted().toList());
    }

}
