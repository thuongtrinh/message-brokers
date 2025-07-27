package com.txt.eda.engine.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.txt.eda.engine.entities.TransactionConfig;
import com.txt.eda.engine.repositories.TransactionConfigRespository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.txt.eda.engine.constant.FlowableConstants.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class InitialProcessDataCSService {

    private final RuntimeService runtimeService;
    private final TransactionConfigRespository transactionConfigRespository;

    @Value("${syspro-version}")
    String version;


    public void execute(DelegateExecution execution) {
        execution.getParent().setVariable(FIX, "FIX");
        execution.getParent().setVariable(IS_LOGIC_CHECK, Boolean.FALSE);
        ProcessInstanceInitVariable processInstanceInitVariable = getProcessInstanceInitVariable(execution);
        TransactionConfig transactionConfig = getTransactionWorkflowConfig(processInstanceInitVariable, version);
    }

    private TransactionConfig getTransactionWorkflowConfig(ProcessInstanceInitVariable processInstanceInitVariable, String version) {
        List<TransactionConfig> configList = transactionConfigRespository
                .findAllByVersionAndTransType(version, processInstanceInitVariable.getTransType());

        if (configList == null || configList.size() == 0) {
            throw new RuntimeException("Engine - No transaction config found, Make sure transaction type '" +
                    processInstanceInitVariable.getTransType() + "' is configured");
        }

        return configList.get(0);
    }

    private ProcessInstanceInitVariable getProcessInstanceInitVariable(DelegateExecution execution) throws RuntimeException {
        try {
            return new ProcessInstanceInitVariable(
                    Optional.of(execution.getVariable(FIX)).get().toString(),
                    Optional.of(execution.getVariable(TRANS_TYPE)).get().toString(),
                    Optional.of(execution.getVariable(CORRELATION_ID)).get().toString(),
                    Optional.of(execution.getVariable(RESOURCE)).get().toString());
        } catch (NullPointerException e) {
            throw new RuntimeException("Invalid process instance initial variable", e);
        }
    }

    @Data
    @AllArgsConstructor
    private class ProcessInstanceInitVariable {
        private String fix;
        private String transType;
        private String correlationId;
        private String resource;
    }
}
