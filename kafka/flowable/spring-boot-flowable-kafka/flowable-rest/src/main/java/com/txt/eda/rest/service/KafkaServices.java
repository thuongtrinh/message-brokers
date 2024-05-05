package com.txt.eda.rest.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.eda.common.dto.common.RequestDTO;
import com.txt.eda.common.dto.common.TransactionCS;
import com.txt.eda.common.dto.common.TransactionSys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;


@Service
@Slf4j
public class KafkaServices<T extends RequestDTO> {

    private static final String EXCHANGE_ID = "exchangeId";
    private static final String CORRELATION_ID = "correlationId";

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;

    public KafkaServices(KafkaTemplate<Object, Object> kafkaTemplate, Environment env, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.env = env;
        this.objectMapper = objectMapper;
    }

    @Async
    @Retryable(label = "sendDataToTopic", maxAttempts = Short.MAX_VALUE, backoff = @Backoff(delay = 5000))
    public void sendDataToTopic(TransactionSys data, String topic) {
        String dataStr = "";

        try {
            String payload = objectMapper.writeValueAsString(data.getPayload());

            String exchangeId = data.getExchangeId();
            if (StringUtils.isBlank(exchangeId)) {
                JsonNode jsonNode = objectMapper.readTree(payload);
                exchangeId = jsonNode.get(EXCHANGE_ID) == null ? UUID.randomUUID().toString() : jsonNode.get(EXCHANGE_ID).asText();
                data.setExchangeId(exchangeId);
            }
            MDC.put("exchangeId", exchangeId);

            String correlationId = data.getCorrelationId();
            if (StringUtils.isBlank(correlationId)) {
                JsonNode jsonNode = objectMapper.readTree(payload);
                correlationId = jsonNode.get(CORRELATION_ID) == null ? UUID.randomUUID().toString() : jsonNode.get(CORRELATION_ID).asText();
                data.setCorrelationId(correlationId);
            }
            MDC.put("correlationId", correlationId);

            //convert data to string
            data.setPayload(payload);
            dataStr = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        log.info("Send to topic {} with data {}", topic, data);

        kafkaTemplate.send(topic, dataStr).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Send data to Kafka topic {} failured", topic);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                log.warn("Send data to Kafka topic {} successfully", topic);
            }
        });
    }

    @Async
    @Retryable(label = "sendDataToTopic", maxAttempts = Short.MAX_VALUE, backoff = @Backoff(delay = 5000))
    public void sendDataToTopicCS(TransactionCS data, String topic) {
        String dataStr = "";

        try {
            String payload = objectMapper.writeValueAsString(data.getPayload());

            String exchangeId = data.getExchangeId();
            if (StringUtils.isBlank(exchangeId)) {
                JsonNode jsonNode = objectMapper.readTree(payload);
                exchangeId = jsonNode.get(EXCHANGE_ID) == null ? UUID.randomUUID().toString() : jsonNode.get(EXCHANGE_ID).asText();
                data.setExchangeId(exchangeId);
            }
            MDC.put("exchangeId", exchangeId);

            String correlationId = data.getCorrelationId();
            if (StringUtils.isBlank(correlationId)) {
                JsonNode jsonNode = objectMapper.readTree(payload);
                correlationId = jsonNode.get(CORRELATION_ID) == null ? UUID.randomUUID().toString() : jsonNode.get(CORRELATION_ID).asText();
                data.setCorrelationId(correlationId);
            }
            MDC.put("correlationId", correlationId);

            //convert data to string
            data.setPayload(payload);
            dataStr = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        log.info("Send to topic {} with data {}", topic, data);

        kafkaTemplate.send(topic, dataStr).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Send data to Kafka topic {} failured", topic);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                log.warn("Send data to Kafka topic {} successfully", topic);
            }
        });
    }

}
