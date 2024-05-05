package com.txt.eda.engine.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.history.async.HistoryJsonConstants;
import org.flowable.engine.impl.history.async.json.transformer.*;
import org.flowable.job.service.impl.history.async.AsyncHistoryJobHandler;
import org.flowable.job.service.impl.history.async.AsyncHistoryJobNotApplicableException;
import org.flowable.job.service.impl.history.async.transformer.HistoryJsonTransformer;
import org.flowable.job.service.impl.persistence.entity.HistoryJobEntity;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaAsyncHistoryJobHandler extends AsyncHistoryJobHandler {

    @Value("${process-event-log.topic}")
    private String topic;


    private final SpringProcessEngineConfiguration springProcessEngineConfiguration;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaAsyncHistoryJobHandler(SpringProcessEngineConfiguration springProcessEngineConfiguration, KafkaTemplate<String, String> kafkaTemplate) {
        super(HistoryJsonConstants.JOB_HANDLER_TYPE_DEFAULT_ASYNC_HISTORY);
        this.kafkaTemplate = kafkaTemplate;
        this.springProcessEngineConfiguration = springProcessEngineConfiguration;
        List<HistoryJsonTransformer> allHistoryJsonTransformers = new ArrayList<>(initDefaultHistoryJsonTransformers());
        allHistoryJsonTransformers.forEach(this::addHistoryJsonTransformer);
    }

    //ProcessEngineConfigurationImpl.initHistoryJobHandlers()
    protected List<HistoryJsonTransformer> initDefaultHistoryJsonTransformers() {
        List<HistoryJsonTransformer> historyJsonTransformers = new ArrayList<>();
        historyJsonTransformers.add(new ProcessInstanceStartHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ProcessInstanceEndHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ProcessInstanceDeleteHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ProcessInstanceDeleteHistoryByProcessDefinitionIdJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ProcessInstancePropertyChangedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new SubProcessInstanceStartHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new SetProcessDefinitionHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new UpdateProcessDefinitionCascadeHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new ActivityStartHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ActivityEndHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ActivityFullHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new ActivityUpdateHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new TaskCreatedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new TaskEndedHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new TaskPropertyChangedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new TaskAssigneeChangedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new TaskOwnerChangedHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new IdentityLinkCreatedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new IdentityLinkDeletedHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new EntityLinkCreatedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new EntityLinkDeletedHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new VariableCreatedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new VariableUpdatedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new VariableRemovedHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new HistoricDetailVariableUpdateHistoryJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new FormPropertiesSubmittedHistoryJsonTransformer(springProcessEngineConfiguration));

        historyJsonTransformers.add(new HistoricUserTaskLogRecordJsonTransformer(springProcessEngineConfiguration));
        historyJsonTransformers.add(new HistoricUserTaskLogDeleteJsonTransformer(springProcessEngineConfiguration));
        return historyJsonTransformers;
    }

    @Override
    protected void processHistoryJson(CommandContext commandContext, HistoryJobEntity job, JsonNode historyNode) {

        String type = null;
        if (historyNode.has(HistoryJsonTransformer.FIELD_NAME_TYPE)) {
            type = historyNode.get(HistoryJsonTransformer.FIELD_NAME_TYPE).asText();
        }
        ObjectNode historicalJsonData = (ObjectNode) historyNode.get(HistoryJsonTransformer.FIELD_NAME_DATA);

        if (logger.isTraceEnabled()) {
            logger.trace("Handling async history job (id={}, type={})", job.getId(), type);
        }

        List<HistoryJsonTransformer> transformers = historyJsonTransformers.get(type);
        if (transformers != null && !transformers.isEmpty()) {
            executeHistoryTransformers(commandContext, job, historicalJsonData, transformers, historyNode);
        } else {
            handleNoMatchingHistoryTransformer(commandContext, job, historicalJsonData, type);
        }
    }

    protected void executeHistoryTransformers(CommandContext commandContext, HistoryJobEntity job, ObjectNode historicalJsonData, List<HistoryJsonTransformer> transformers, JsonNode historyNode) {
        for (HistoryJsonTransformer transformer : transformers) {
            if (transformer.isApplicable(historicalJsonData, commandContext)) {
                transformer.transformJson(job, historicalJsonData, commandContext);
                sendToKafka(historyNode, job, transformer);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not handle history job (id={}) for transformer {}. as it is not applicable. Unacquiring. {}", job.getId(), transformer.getTypes(), historicalJsonData);
                }
                throw new AsyncHistoryJobNotApplicableException("Job is not applicable for transformer types: " + transformer.getTypes());
            }
        }
    }

    protected void handleNoMatchingHistoryTransformer(CommandContext commandContext, HistoryJobEntity job, ObjectNode historicalData, String type, JsonNode historyNode) {
        if (defaultHistoryJsonTransformer != null) {
            if (defaultHistoryJsonTransformer.isApplicable(historicalData, commandContext)) {
                defaultHistoryJsonTransformer.transformJson(job, historicalData, commandContext);
                sendToKafka(historyNode, job, defaultHistoryJsonTransformer);
            } else {
                throw new AsyncHistoryJobNotApplicableException("Job is not applicable for default history json transformer types: " + defaultHistoryJsonTransformer.getTypes());
            }
        } else {
            throw new AsyncHistoryJobNotApplicableException("Cannot transform history json: no transformers found for type " + type);
        }
    }

    private void sendToKafka(JsonNode historicalJsonData, HistoryJobEntity job, HistoryJsonTransformer transformer) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(historicalJsonData);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            logger.error("Could not handle history job (id={}) for transformer {}. as it is not applicable. Unacquiring. {}", job.getId(), transformer.getTypes(), historicalJsonData, e);
        }
    }

}
