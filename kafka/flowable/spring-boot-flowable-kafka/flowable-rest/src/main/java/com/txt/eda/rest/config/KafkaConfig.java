package com.txt.eda.rest.config;

import org.flowable.eventregistry.api.EventRegistry;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.flowable.eventregistry.model.ChannelModel;
import org.flowable.eventregistry.model.KafkaOutboundChannelModel;
import org.flowable.eventregistry.spring.kafka.KafkaChannelDefinitionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaOperations;

@Configuration
public class KafkaConfig {

    class CustomKafkaDefinitionProcessor extends KafkaChannelDefinitionProcessor {

        @Override
        public void registerChannelModel(ChannelModel channelModel, String tenantId, EventRegistry eventRegistry,
                                         EventRepositoryService eventRepositoryService, boolean fallbackToDefaultTenant) {

            if (channelModel instanceof KafkaOutboundChannelModel) {
                processOutboundDefinition((KafkaOutboundChannelModel) channelModel);
            }
        }
    }

    @Bean("kafkaChannelDefinitionProcessor")
    public KafkaChannelDefinitionProcessor kafkaChannelDefinitionProcessor(KafkaListenerEndpointRegistry endpointRegistry,
                                                                           KafkaOperations<Object, Object> kafkaOperations) {
        CustomKafkaDefinitionProcessor customKafkaDefinitionProcessor = new CustomKafkaDefinitionProcessor();
        customKafkaDefinitionProcessor.setEndpointRegistry(endpointRegistry);
        customKafkaDefinitionProcessor.setKafkaOperations(kafkaOperations);

        return customKafkaDefinitionProcessor;
    }
}
