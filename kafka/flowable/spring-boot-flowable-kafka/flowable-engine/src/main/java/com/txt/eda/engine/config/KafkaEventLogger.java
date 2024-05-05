package com.txt.eda.engine.config;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.impl.event.logger.EventFlusher;
import org.flowable.engine.impl.event.logger.EventLogger;
import org.flowable.engine.impl.event.logger.handler.EventLoggerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventLogger extends EventLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventLogger.class);

    @Autowired
    private KafkaEventLogFlusher kafkaEventLogFlusher;

    public KafkaEventLogger() {
        super();
    }

    @Override
    protected EventFlusher createEventFlusher() {
        return kafkaEventLogFlusher;
    }

    @Override
    protected EventLoggerEventHandler instantiateEventHandler(FlowableEvent event,
                                                              Class<? extends EventLoggerEventHandler> eventHandlerClass) {
        try {
            EventLoggerEventHandler eventHandler = eventHandlerClass.newInstance();
            eventHandler.setEvent(event);
            return eventHandler;
        } catch (Exception e) {
            LOGGER.warn("Could not instantiate {}, this is most likely a programmatic error", eventHandlerClass, e);
        }
        return null;
    }
}
