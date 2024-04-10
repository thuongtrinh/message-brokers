package com.txt.flinkstream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.flinkstream.dto.TransactionDTO;
import lombok.SneakyThrows;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;

public class FlinkRequestStream {
    private static final Logger logger = LoggerFactory.getLogger(FlinkRequestStream.class);

    private static String inputTopic = "flink_input";
    private static String outputTopic = "flink_output";
    private static String groupId = "flink_group";
    private static String addressKafka = "localhost:9092";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        logger.info("Kafka bootstrap servers: {}, Group id: {}", addressKafka, groupId);
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, addressKafka);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(inputTopic, new SimpleStringSchema(), kafkaProps);
        kafkaConsumer.setStartFromEarliest();
        logger.info("Obj log topic: {}", inputTopic);

        // jackson
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // get data from topic in
        DataStream<String> bizObjStream = env.addSource(kafkaConsumer).name("Data from Kafka " + inputTopic + " - " + addressKafka);
        bizObjStream.rebalance();

        DataStream<TransactionDTO> dataStream = bizObjStream.map(new MapFunction<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public TransactionDTO map(String str) {
                TransactionDTO transactionDTO = convertTransactionOpsChangeDespathAddress();
                logger.info("TransactionDTO was transformed: {}", transactionDTO);
                return transactionDTO;
            }
        });

        // create a Kafka producer to topic out
        final int kafkaTransactionTimeout = 300000;
        kafkaProps.put("transaction.timeout.ms", kafkaTransactionTimeout);
        logger.info("Oj out topic: {}, Kafka transaction timeout: {}", outputTopic, kafkaTransactionTimeout);
        FlinkKafkaProducer<TransactionDTO> kafkaProducer = new FlinkKafkaProducer<>(outputTopic, new KafkaSerializationSchema<TransactionDTO>() {
            @SneakyThrows
            @Override
            public ProducerRecord<byte[], byte[]> serialize(TransactionDTO transactionOps, Long aLong) {
                logger.info("TransactionDTO SEND TO KAFKA: {}", transactionOps);
                return new ProducerRecord<byte[], byte[]>(outputTopic, objectMapper.writeValueAsString(transactionOps).getBytes(StandardCharsets.UTF_8));
            }
        }, kafkaProps, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        // versions 0.10+ allow attaching the records' event timestamp when writing them to Kafka;
        // this method is not available for earlier Kafka versions
        kafkaProducer.setWriteTimestampToKafka(true);

        dataStream.addSink(kafkaProducer).name("Data to Kafka " + outputTopic + " - " + addressKafka);

        env.execute("Data Request Stream");
    }

    private static TransactionDTO convertTransactionOpsChangeDespathAddress() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSource("BAK");
        transactionDTO.setExchangeId(UUID.randomUUID().toString());
        transactionDTO.setStatus("SUCCESSFUL");
        transactionDTO.setAddress("SSG");
        transactionDTO.setBody("body test");
        transactionDTO.setType("TP");
        return transactionDTO;
    }

}
