package com.txt.eda.engine.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.eventregistry.api.EventRegistry;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.flowable.eventregistry.model.ChannelEventTenantIdDetection;
import org.flowable.eventregistry.model.ChannelModel;
import org.flowable.eventregistry.model.KafkaInboundChannelModel;
import org.flowable.eventregistry.model.KafkaOutboundChannelModel;
import org.flowable.eventregistry.spring.kafka.KafkaChannelDefinitionProcessor;
import org.flowable.eventregistry.spring.kafka.KafkaOperationsOutboundEventChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CustomKafkaChannelDefinitionConfiguation {
    @org.springframework.beans.factory.annotation.Value("${syspro-tenant-id}")
    private String tenant;

    private String tenantOutbound;

    private List<String> listChannelRegisted = new ArrayList<>();

    class CustomKafkaDefinitionProcessor extends KafkaChannelDefinitionProcessor {

        @Override
        protected void processOutboundDefinition(KafkaOutboundChannelModel channelModel) {
            Object resolvedValue = super.resolveExpression(channelModel.getTopic());
            String topic = this.resolveTopic(resolvedValue);

            // Check if channel tenant in postgres equal to channel tenant in engine
            if (channelModel.getOutboundEventChannelAdapter() == null && StringUtils.hasText(topic) && topic.contains(tenantOutbound)) {
                channelModel.setOutboundEventChannelAdapter(new KafkaOperationsOutboundEventChannelAdapter(
                        kafkaOperations, topic, channelModel.getRecordKey()));
            }
        }

        public String resolveExpression(String value) {
            return (String) super.resolveExpression(value);
        }

        protected String resolveTopic(Object resolvedValue) {
            if (resolvedValue instanceof String) {
                return (String) resolvedValue;
            } else {
                throw new IllegalArgumentException(
                        "Channel definition cannot resolve " + resolvedValue + " as a String");
            }
        }

        @Override
        public void registerChannelModel(ChannelModel channelModel, String tenantId, EventRegistry eventRegistry,
                                         EventRepositoryService eventRepositoryService, boolean fallbackToDefaultTenant) {
            if (channelModel instanceof KafkaInboundChannelModel) {
                KafkaInboundChannelModel kafkaChannelModel = (KafkaInboundChannelModel) channelModel;

                if (tenantId.equals(tenant)) {
                    ChannelEventTenantIdDetection channelEventTenantIdDetection = new ChannelEventTenantIdDetection();
                    channelEventTenantIdDetection.setFixedValue(tenantId);
                    kafkaChannelModel.setChannelEventTenantIdDetection(channelEventTenantIdDetection);
                    KafkaListenerEndpoint endpoint = createKafkaListenerEndpoint(kafkaChannelModel, tenantId, eventRegistry);
                    if (!listChannelRegisted.contains(endpoint.getId())) {
                        listChannelRegisted.add(endpoint.getId());
                        registerEndpoint(endpoint, null);
                    }
                }
            } else if (channelModel instanceof KafkaOutboundChannelModel) {
                tenantOutbound = tenantId;
                processOutboundDefinition((KafkaOutboundChannelModel) channelModel);
            }
        }
    }

    @Bean("kafkaChannelDefinitionProcessor")
    public KafkaChannelDefinitionProcessor kafkaChannelDefinitionProcessor(KafkaListenerEndpointRegistry endpointRegistry, KafkaOperations<Object, Object> kafkaOperations) {
        CustomKafkaDefinitionProcessor customKafkaDefinitionProcessor = new CustomKafkaDefinitionProcessor();
        customKafkaDefinitionProcessor.setEndpointRegistry(endpointRegistry);
        customKafkaDefinitionProcessor.setKafkaOperations(kafkaOperations);
        return customKafkaDefinitionProcessor;
    }
}
