package com.txt.eda.engine.config;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.flowable.eventregistry.api.OutboundEventChannelAdapter;
import org.flowable.eventregistry.api.OutboundEventProcessingPipeline;
import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.impl.DefaultOutboundEventProcessor;
import org.flowable.eventregistry.model.ChannelModel;
import org.flowable.eventregistry.model.KafkaOutboundChannelModel;
import org.flowable.eventregistry.spring.kafka.KafkaOperationsOutboundEventChannelAdapter;

import java.util.Collection;

public class CustomDefaultOutboundEventProcessor extends DefaultOutboundEventProcessor {

    private CustomKafkaChannelDefinitionConfiguation.CustomKafkaDefinitionProcessor customKafkaDefinitionProcessor;

    public CustomDefaultOutboundEventProcessor(EventRepositoryService eventRepositoryService, boolean fallbackToDefaultTenant, CustomKafkaChannelDefinitionConfiguation.CustomKafkaDefinitionProcessor customKafkaDefinitionProcessor) {
        super(eventRepositoryService, fallbackToDefaultTenant);
        this.customKafkaDefinitionProcessor = customKafkaDefinitionProcessor;

    }

    @Override
    public void sendEvent(EventInstance eventInstance, Collection<ChannelModel> channelModels) {
        if (channelModels == null || channelModels.isEmpty()) {
            throw new FlowableException("No channel model set for outgoing event " + eventInstance.getEventKey());
        }

        for (ChannelModel channelModel : channelModels) {

            KafkaOutboundChannelModel outboundChannelModel = (KafkaOutboundChannelModel) channelModel;

            OutboundEventProcessingPipeline<?> outboundEventProcessingPipeline = (OutboundEventProcessingPipeline<?>) outboundChannelModel.getOutboundEventProcessingPipeline();
            Object rawEvent = outboundEventProcessingPipeline.run(eventInstance);

            OutboundEventChannelAdapter outboundEventChannelAdapter = (OutboundEventChannelAdapter<?>) outboundChannelModel.getOutboundEventChannelAdapter();
            if (outboundEventChannelAdapter == null) {
                Object resolvedValue = customKafkaDefinitionProcessor.resolveExpression(outboundChannelModel.getTopic());
                String topic = customKafkaDefinitionProcessor.resolveTopic(resolvedValue);
                outboundEventChannelAdapter = (new KafkaOperationsOutboundEventChannelAdapter(customKafkaDefinitionProcessor.getKafkaOperations()
                        , topic, outboundChannelModel.getRecordKey()));
            }

            outboundEventChannelAdapter.sendEvent(rawEvent);
        }
    }
}
