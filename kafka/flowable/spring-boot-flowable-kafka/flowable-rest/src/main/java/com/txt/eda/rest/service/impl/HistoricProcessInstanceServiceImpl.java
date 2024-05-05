package com.txt.eda.rest.service.impl;

import com.txt.eda.rest.dto.processInstance.HistoricProcessInstanceDTO;
import com.txt.eda.rest.service.HistoricProcessInstanceService;
import com.txt.eda.rest.util.ProcessInstanceUtils;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.rest.service.api.BpmnRestApiInterceptor;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.history.HistoricProcessInstanceResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class HistoricProcessInstanceServiceImpl implements HistoricProcessInstanceService {

    @Autowired
    protected HistoryService historyService;

    @Autowired(required = false)
    protected BpmnRestApiInterceptor restApiInterceptor;

    @Autowired
    protected RestResponseFactory restResponseFactory;


    @Override
    public HistoricProcessInstanceDTO getHistoricProcessInstance(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();

        if (historicProcessInstance == null) {
            throw new FlowableObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.", HistoricProcessInstance.class);
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.accessHistoryProcessInfoById(historicProcessInstance);
        }

        HistoricProcessInstanceResponse historicProcessInstanceResponse =
                restResponseFactory.createHistoricProcessInstanceResponse(historicProcessInstance);

        HistoricProcessInstanceDTO response = new HistoricProcessInstanceDTO();
        BeanUtils.copyProperties(historicProcessInstanceResponse, response);

        if (!CollectionUtils.isEmpty(historicProcessInstanceResponse.getVariables())) {
            ProcessInstanceUtils.convertGenericProcessInstance(response, historicProcessInstanceResponse.getVariables());
        }

        return response;

    }
}
