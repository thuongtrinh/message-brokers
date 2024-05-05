package com.txt.eda.rest.service.impl;

import com.txt.eda.rest.constant.Constants;
import com.txt.eda.rest.dto.payload.PayLoadSaveInfoResponse;
import com.txt.eda.rest.dto.payload.PayloadSaveRequest;
import com.txt.eda.rest.service.CommonProcessInstanceService;
import com.txt.eda.rest.service.HistoricProcessInstanceService;
import com.txt.eda.rest.service.PayloadInfoService;
import com.txt.eda.rest.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class PayloadInfoServiceImpl implements PayloadInfoService {
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final HistoricProcessInstanceService historicProcessInstanceService;
    private final CommonProcessInstanceService commonProcessInstanceService;


    @Override
    public PayLoadSaveInfoResponse getPayLoadSaveInfo(String processInstanceId) {
        String parentProcessInstanceId = commonProcessInstanceService.getParentProcessInstanceId(processInstanceId);
        PayLoadSaveInfoResponse response = new PayLoadSaveInfoResponse();
        response.setProcessInstanceId(parentProcessInstanceId);
        /*List<PayloadInfoLog> infos = payloadInfoRepository
                .getPayloadSaveInfoLog
                        (parentProcessInstanceId,Boolean.FALSE, version);
        if(!CollectionUtils.isEmpty(infos)) {
            PayloadInfoLog info = infos.get(0);
            response.setPayload(this.decryptPayload(info.getPayLoad(), info.getVtsIV(), vtsService));
            response.setCreatedBy(info.getCreatedBy());
            response.setCreatedDate(info.getCreatedDate());
        }*/
        return response;
    }

    @Override
    public PayLoadSaveInfoResponse savePayload(PayloadSaveRequest request, String taskId) {
        PayLoadSaveInfoResponse response = new PayLoadSaveInfoResponse();
        String parentProcessInstanceId = commonProcessInstanceService
                .getParentProcessInstanceId(request.getProcessInstanceId());
        Map<String, Object> variables = runtimeService.getVariables(request.getProcessInstanceId());
        String transType = (String) variables.get(Constants.TRANS_TYPE);

        runtimeService.setVariable(request.getProcessInstanceId(), Constants.PAYLOAD, JsonHelper.objectToJson(request.getPayload()));

        response.setPayload(request.getPayload());
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .includeProcessVariables()
                .singleResult();

        return response;
    }

}
