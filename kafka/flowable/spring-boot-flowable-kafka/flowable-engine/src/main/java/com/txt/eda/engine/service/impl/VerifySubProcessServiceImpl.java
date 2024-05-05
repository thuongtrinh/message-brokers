package com.txt.eda.engine.service.impl;

import com.txt.eda.engine.service.VerifySubProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.txt.eda.engine.constant.FlowableConstants.*;


@Service("verifySubProcessService")
@RequiredArgsConstructor
@Slf4j
public class VerifySubProcessServiceImpl implements VerifySubProcessService {

    private final RuntimeService runtimeService;

    @Override
    public void initVariable(DelegateExecution execution) {
        ProcessInstance wfProcessInstance = this.getProcessInstance(execution);
        this.setSubProcessEventVariables(execution, wfProcessInstance);
    }

    private ProcessInstance getProcessInstance(DelegateExecution execution) {
        String wfProcessInstanceId = execution.getVariable("processInstanceId", String.class);
        String parentId = execution.getVariable("parentId", String.class);

        System.out.println("processInstanceId : " + wfProcessInstanceId);
        System.out.println("parentId : " + parentId);

        ProcessInstance wfProcessInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(wfProcessInstanceId)
                .active()
                .singleResult();
        if (wfProcessInstance == null) {
            throw new RuntimeException("Cannot find active work flow process instance, unable to start process");
        }
        return wfProcessInstance;
    }

    private void setSubProcessEventVariables(DelegateExecution execution, ProcessInstance wfProcessInstance) {
        Map<String, Object> wfProcessVariables = runtimeService.getVariables(wfProcessInstance.getId());
        wfProcessVariables.put(PROCESS_INSTANCE_ID, wfProcessInstance.getProcessInstanceId());
        execution.getParent().setVariables(wfProcessVariables);
    }

    @Override
    public void returnProcess(DelegateExecution execution) {
        /*ProcessInstance wfProcessInstance = this.getProcessInstance(execution);
        String wfProcessInstanceId = wfProcessInstance.getId();

        Map<String, Object> variables = execution.getParent().getVariables();
        variables.remove(PROCESS_INSTANCE_ID);

        //remove event key
        Map<String, Object> returnVariables = new HashMap<>();
        variables.forEach((key, value) -> {
            returnVariables.put(key, value);
        });
        runtimeService.setVariables(wfProcessInstanceId, returnVariables);*/
    }

}
