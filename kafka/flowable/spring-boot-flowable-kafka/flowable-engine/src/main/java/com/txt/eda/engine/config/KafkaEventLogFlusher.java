package com.txt.eda.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.event.logger.AbstractEventFlusher;
import org.flowable.engine.impl.event.logger.handler.EventLoggerEventHandler;
import org.flowable.engine.impl.persistence.entity.EventLogEntryEntity;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class KafkaEventLogFlusher extends AbstractEventFlusher {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventLogFlusher.class);

    @Value("${process-event-log.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final SpringProcessEngineConfiguration springProcessEngineConfiguration;

    public KafkaEventLogFlusher(KafkaTemplate<String, String> kafkaTemplate, SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        this.kafkaTemplate = kafkaTemplate;
        this.springProcessEngineConfiguration = springProcessEngineConfiguration;
    }

    @Override
    public void closing(CommandContext commandContext) {

        if (commandContext.getException() != null) {
            return; // Not interested in events about exceptions
        }

        if (CollectionUtils.isEmpty(eventHandlers)) {
            LOGGER.warn("Event Handlers is empty");
            return;
        }

        for (EventLoggerEventHandler eventHandler : eventHandlers) {
            String eventLog = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                eventHandler.setTimeStamp(springProcessEngineConfiguration.getClock().getCurrentTime());
                eventHandler.setObjectMapper(objectMapper);
                EventLogEntryEntity entity = eventHandler.generateEventLogEntry(commandContext);
                eventLog = objectMapper.writeValueAsString(entity);
                kafkaTemplate.send(topic, eventLog);
            } catch (Exception e) {
                LOGGER.error("Could not create event_log={}", eventLog, e);
            }
        }
    }

    @Override
    public void afterSessionsFlush(CommandContext commandContext) {

    }

    @Override
    public void closeFailure(CommandContext commandContext) {

    }

    @Override
    public Integer order() {
        return 100;
    }

    @Override
    public boolean multipleAllowed() {
        return false;
    }

}
