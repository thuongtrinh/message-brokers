package com.txt.eda.rest.service.impl;

import com.txt.eda.rest.constant.Constants;
import com.txt.eda.rest.dto.processInstance.ProcessInstanceDTO;
import com.txt.eda.rest.dto.processInstance.ProcessInstanceRequestDTO;
import com.txt.eda.rest.dto.processInstance.ProcessInstanceResponseDTO;
import com.txt.eda.rest.service.ProcessInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.query.QueryProperty;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.engine.impl.ProcessInstanceQueryProperty;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.rest.service.api.engine.variable.QueryVariable;
import org.flowable.rest.service.api.runtime.process.BaseProcessInstanceResource;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceQueryRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessInstanceServiceImpl extends BaseProcessInstanceResource implements ProcessInstanceService {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<>();

    static {
        allowedSortProperties.put("processDefinitionId", ProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
        allowedSortProperties.put("processDefinitionKey", ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
        allowedSortProperties.put("id", ProcessInstanceQueryProperty.PROCESS_INSTANCE_ID);
        allowedSortProperties.put("startTime", ProcessInstanceQueryProperty.PROCESS_START_TIME);
        allowedSortProperties.put("tenantId", ProcessInstanceQueryProperty.TENANT_ID);
    }

    @Override
    public ProcessInstanceResponseDTO queryProcessInstance(ProcessInstanceRequestDTO queryRequest) {
        ProcessInstanceQueryRequest flowableQueryRequest = getFlowableQueryProcessInstanceRequest(queryRequest);
        ProcessInstanceQuery flowableProcessQuery = getFlowableProcessQuery(queryRequest);

        DataResponse<ProcessInstanceResponse> flowableQueryResponse =
                paginateList(new HashMap<>(), flowableQueryRequest, flowableProcessQuery, "startTime", allowedSortProperties,
                        restResponseFactory::createProcessInstanceResponseList);

        return getQueryProcessInstanceResponse(flowableQueryResponse);
    }

    private ProcessInstanceQueryRequest getFlowableQueryProcessInstanceRequest(ProcessInstanceRequestDTO queryRequest) {
        ProcessInstanceQueryRequest flowableQueryRequest = new ProcessInstanceQueryRequest();
        BeanUtils.copyProperties(queryRequest, flowableQueryRequest);
        return flowableQueryRequest;
    }

    private ProcessInstanceQuery getFlowableProcessQuery(ProcessInstanceRequestDTO queryRequest) {
        ProcessInstanceQuery processQuery = runtimeService.createProcessInstanceQuery();

        if (queryRequest.getCreatedBefore() != null) {
            processQuery.startedBefore(queryRequest.getCreatedBefore());
        }
        if (queryRequest.getCreatedAfter() != null) {
            processQuery.startedAfter(queryRequest.getCreatedAfter());
        }

        if(!CollectionUtils.isEmpty(queryRequest.getResourceIn())) {
            if(queryRequest.getResourceIn().size() == 1){
                for (String value : queryRequest.getResourceIn()) {
                    processQuery.variableValueEqualsIgnoreCase(Constants.RESOURCE, value);
                }
            }else {
                processQuery.or();
                for (String value : queryRequest.getResourceIn()) {
                    processQuery.variableValueEqualsIgnoreCase(Constants.RESOURCE, value);
                }
                processQuery.endOr();
            }
        }

        if(!CollectionUtils.isEmpty(queryRequest.getTransTypeIn())) {
            if(queryRequest.getTransTypeIn().size() == 1){
                for (String value : queryRequest.getTransTypeIn()) {
                    processQuery.variableValueEqualsIgnoreCase(Constants.TRANS_TYPE, value);
                }
            }else {
                processQuery.or();
                for (String value : queryRequest.getTransTypeIn()) {
                    processQuery.variableValueEqualsIgnoreCase(Constants.TRANS_TYPE, value);
                }
                processQuery.endOr();
            }

        }

//        if(!ObjectUtils.isEmpty(queryRequest.getCorrelationId())) {
//            processQuery.or();
//            processQuery.variableValueLikeIgnoreCase(Constants.CLIENT_NUMBER, queryRequest.getCorrelationId());
//            processQuery.variableValueLikeIgnoreCase(Constants.POLICY_NUMBER, queryRequest.getCorrelationId());
//            processQuery.endOr();
//        }

        if(queryRequest.getSuspended() != null && queryRequest.getSuspended()) {
            processQuery.suspended();
        } else {
            processQuery.active();
        }

        List<QueryVariable> variables = getQueryProcessInstanceQueryVariable(queryRequest);
        if (!CollectionUtils.isEmpty(variables)) {
            addVariables(processQuery, variables);
        }

        processQuery.includeProcessVariables();

        return processQuery;
    }

    private List<QueryVariable> getQueryProcessInstanceQueryVariable(ProcessInstanceRequestDTO queryRequest) {
        List<QueryVariable> variables = new ArrayList<>();

        if (StringUtils.isNotEmpty(queryRequest.getBusinessKey()))
            variables.add(getStringQueryVariable(Constants.BUSINESS_KEY,
                    QueryVariable.QueryVariableOperation.LIKE_IGNORE_CASE.getFriendlyName(),
                    "%" + queryRequest.getBusinessKey() + "%"));

        if (StringUtils.isNotEmpty(queryRequest.getCategory()))
            variables.add(getStringQueryVariable(Constants.CATEGORY,
                    QueryVariable.QueryVariableOperation.EQUALS_IGNORE_CASE.getFriendlyName(),
                    queryRequest.getCategory()));

        return variables;
    }

    private QueryVariable getStringQueryVariable(String name, String queryOperation, String value) {
        QueryVariable queryVariable = new QueryVariable();
        queryVariable.setName(name);
        queryVariable.setType("string");
        queryVariable.setOperation(queryOperation);
        queryVariable.setValue(value);
        return queryVariable;
    }

    private ProcessInstanceResponseDTO getQueryProcessInstanceResponse(
            DataResponse<org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse> flowableQueryResponse) {
        ProcessInstanceResponseDTO queryResponse = new ProcessInstanceResponseDTO();
        String ignoreProperties = "data";
        BeanUtils.copyProperties(flowableQueryResponse, queryResponse, ignoreProperties);

        if (!CollectionUtils.isEmpty(flowableQueryResponse.getData())) {
            List<ProcessInstanceDTO> processInstances = flowableQueryResponse.getData().stream()
                    .map(processInstanceResponse -> {
                        ProcessInstanceDTO processInstance = new ProcessInstanceDTO();
                        BeanUtils.copyProperties(processInstanceResponse, processInstance);
//                        if (!CollectionUtils.isEmpty(processInstanceResponse.getVariables())) {
//                            ProcessInstanceUtil.convertGenericProcessInstance(processInstance,
//                                    processInstanceResponse.getVariables());
//                            for (RestVariable queryVariable :  processInstanceResponse.getVariables()) {
//                                if(!ObjectUtils.isEmpty(queryVariable.getValue())) {
//                                    if (CREATED_DATE.equals(queryVariable.getName())) {
//                                        processInstance.setCreatedDate(DatetimeUtil.parseISO8601ToDate(queryVariable.getValue().toString()));
//                                    } else if (CH_NEW_TYPE.equalsIgnoreCase(queryVariable.getName())) {
//                                        if (!ObjectUtils.isEmpty(queryVariable.getValue())) {
//                                            processInstance.setNewType(queryVariable.getValue().toString());
//                                        }
//                                    } else if (CH_NEW_SUB_TYPE.equalsIgnoreCase(queryVariable.getName())) {
//                                        if (!ObjectUtils.isEmpty(queryVariable.getValue())) {
//                                            processInstance.setNewSubType(queryVariable.getValue().toString());
//                                        }
//                                    } else if (COMMON_REQUEST_TYPE.equalsIgnoreCase(queryVariable.getName())) {
//                                        if (!ObjectUtils.isEmpty(queryVariable.getValue())) {
//                                            processInstance.setRequest(queryVariable.getValue().toString());
//                                        }
//                                    }
//                                }
//                            }
//                        }
                        return processInstance;
                    }).collect(Collectors.toList());

            queryResponse.setData(processInstances);
        }

        return queryResponse;
    }
}
