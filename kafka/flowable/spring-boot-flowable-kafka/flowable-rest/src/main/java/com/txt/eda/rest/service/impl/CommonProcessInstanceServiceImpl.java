package com.txt.eda.rest.service.impl;


import com.txt.eda.rest.service.CommonProcessInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.txt.eda.rest.constant.Constants.*;

import java.util.Map;


@Service("commonProcessInstanceService")
@RequiredArgsConstructor
@Slf4j
public class CommonProcessInstanceServiceImpl implements CommonProcessInstanceService {

    private final RuntimeService runtimeService;

    @Override
    public String getParentProcessInstanceId(String processInstanceId) {
        String parentProcessInstance = processInstanceId;
        try {
            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .includeProcessVariables()
                    .variableExists(PROCESS_INSTANCE_ID)
                    .active()
                    .orderByStartTime()
                    .desc()
                    .singleResult();

            if (!ObjectUtils.isEmpty(processInstance)) {
                Map<String, Object> obj = processInstance.getProcessVariables();
                parentProcessInstance = (String) obj.get(PROCESS_INSTANCE_ID);
                if (StringUtils.isEmpty(parentProcessInstance)) {
                    parentProcessInstance = processInstanceId;
                }
            }
        } catch (Exception e) {
            parentProcessInstance = processInstanceId;
        }

        return parentProcessInstance;
    }

}
