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

    private static String inputTopic = "flink_stream_input";
    private static String outputTopic = "flink_stream_output";
    private static String consumerGroup = "flink_stream_group";
    private static String address = "localhost:9092";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final String kafkaBootStrapServer = address;
        final String groupId = consumerGroup;
        logger.info("Kafka bootstrap servers: {}, Group id: {}", kafkaBootStrapServer, groupId);
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootStrapServer);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // create a kafka consumer to topic workflow bo in
        final String bizObjInTopic = inputTopic;
        FlinkKafkaConsumer<String> bizObjFlinkKafkaConsumer = new FlinkKafkaConsumer<>(bizObjInTopic, new SimpleStringSchema(), kafkaProps);
        //bizObjFlinkKafkaConsumer.setStartFromGroupOffsets();
        bizObjFlinkKafkaConsumer.setStartFromEarliest();
        logger.info("Biz obj log topic: {}", bizObjInTopic);

        // jackson
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // get data from topic in
        DataStream<String> bizObjStream = env.addSource(bizObjFlinkKafkaConsumer).name("Data from Kafka " + bizObjInTopic + " - " + kafkaBootStrapServer);
        bizObjStream.rebalance();

        DataStream<TransactionDTO> bizObjResultStream = bizObjStream.map(new MapFunction<>() {
            private static final long serialVersionUID = -2335483819304911134L;

            @Override
            public TransactionDTO map(String str) throws Exception {
                TransactionDTO transactionDTO = convertTransactionOpsChangeDespathAddress();
                logger.info("TransactionDTO was transformed: {}", transactionDTO);
                return transactionDTO;
            }
        });

        // create a Kafka producer to topic out
        final String bizObjOutTopic = outputTopic;
        final int kafkaTransactionTimeout = 300000;
        kafkaProps.put("transaction.timeout.ms", kafkaTransactionTimeout);
        logger.info("Biz obj out topic: {}, Kafka transaction timeout: {}", bizObjOutTopic, kafkaTransactionTimeout);
        FlinkKafkaProducer<TransactionDTO> bizObjResultFlinkKafkaProducer = new FlinkKafkaProducer<TransactionDTO>(bizObjOutTopic, new KafkaSerializationSchema<TransactionDTO>() {
            @SneakyThrows
            @Override
            public ProducerRecord<byte[], byte[]> serialize(TransactionDTO transactionOps, Long aLong) {
                logger.info("TransactionDTO SEND TO KAFKA: {}", transactionOps);
                return new ProducerRecord<byte[], byte[]>(bizObjOutTopic, objectMapper.writeValueAsString(transactionOps).getBytes(StandardCharsets.UTF_8));
            }
        }, kafkaProps, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        // versions 0.10+ allow attaching the records' event timestamp when writing them to Kafka;
        // this method is not available for earlier Kafka versions
        bizObjResultFlinkKafkaProducer.setWriteTimestampToKafka(true);

        bizObjResultStream.addSink(bizObjResultFlinkKafkaProducer).name("Data to Kafka " + bizObjOutTopic + " - " + kafkaBootStrapServer);

        // create a Kafka producer to topic log
		/* final String bizObjLogTopic = (null == parameter ? "BO-LOG" : parameter.get("kafka.wf.topic.log", "BO-LOG"));
		logger.info("Biz obj log topic {}, Kafka transaction timeout {}", bizObjLogTopic, kafkaTransactionTimeout);
		FlinkKafkaProducer<ResponseResult> bizObjResultLogFlinkKafkaProducer = new FlinkKafkaProducer<ResponseResult>(bizObjLogTopic,
				new KafkaProducerLogSerializationSchema(bizObjLogTopic, objectMapper), kafkaProps,
				FlinkKafkaProducer.Semantic.EXACTLY_ONCE); */

        // versions 0.10+ allow attaching the records' event timestamp when writing them to Kafka;
        // this method is not available for earlier Kafka versions
		/* bizObjResultLogFlinkKafkaProducer.setWriteTimestampToKafka(true);
		bizObjResultStream.addSink(bizObjResultLogFlinkKafkaProducer).name("Data to Kafka " + bizObjLogTopic + " - " + kafkaBootStrapServer); */

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
