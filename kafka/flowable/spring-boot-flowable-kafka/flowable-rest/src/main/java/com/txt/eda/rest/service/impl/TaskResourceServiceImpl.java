package com.txt.eda.rest.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.eda.rest.constant.Constants;
import com.txt.eda.rest.dto.task.TaskActionRequestDTO;
import com.txt.eda.rest.dto.task.TaskDTO;
import com.txt.eda.rest.service.TaskResourceService;
import com.txt.eda.rest.util.ProcessInstanceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.task.Comment;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.task.TaskActionRequest;
import org.flowable.rest.service.api.runtime.task.TaskBaseResource;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskResourceServiceImpl extends TaskBaseResource implements TaskResourceService {

    private final RuntimeService runtimeService;
    private final ManagementService managementService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private HistoryService historyService;
    @Qualifier("entityManagerFactory")
    private final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    @Override
    public TaskDTO getTask(String taskId) {
        TaskResponse flowableTaskResponse = restResponseFactory.createTaskResponse(this.getTaskFromRequest(taskId));
        return getTaskDataFromFlowableTaskResponse(flowableTaskResponse);
    }

    @Override
    protected Task getTaskFromRequest(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
        if (task == null) {
            throw new FlowableObjectNotFoundException("Could not find a task with id '" + taskId + "'.", Task.class);
        }

        if (restApiInterceptor != null) {
            restApiInterceptor.accessTaskInfoById(task);
        }

        return task;
    }

    private TaskDTO getTaskDataFromFlowableTaskResponse(TaskResponse flowableTaskResponse) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(flowableTaskResponse, taskDTO);

        List<RestVariable> restVariables = flowableTaskResponse.getVariables();
        if (!CollectionUtils.isEmpty(restVariables))
            ProcessInstanceUtils.convertDetailGenericProcessInstance(taskDTO, restVariables);

        return taskDTO;
    }

    @Override
    public void executeTaskAction(String taskId, TaskActionRequestDTO taskActionRequest) {
        if (taskActionRequest == null) {
            throw new FlowableException("A request body was expected when executing a task action.");
        }
        Task task = getTaskFromRequest(taskId);
        if (Constants.COMPLETE_ACTION.equals(taskActionRequest.getAction())) {
            completeTask(task, taskActionRequest);
        } else if (Constants.ASSIGN_ACTION.equals(taskActionRequest.getAction())) {
            assignTask(task, taskActionRequest);
        } else {
            throw new FlowableIllegalArgumentException("Invalid action: '" + taskActionRequest.getAction() + "'.");
        }
    }

    private void assignTask(Task task, TaskActionRequestDTO taskActionRequest) {
        taskService.setAssignee(task.getId(), taskActionRequest.getAssignee());
    }

    private void completeTask(Task task, TaskActionRequestDTO taskActionRequest) {
        Map<String, Object> variablesToSet = new HashMap<>();
        variablesToSet.put("update_author", "author_txt");
        variablesToSet.put("modified_date", new Date());
        String parentProcessInstaceId = getParentProcessInstanceId(task);

        Comment createdComment = taskService.addComment(task.getId(), parentProcessInstaceId, taskActionRequest.getComment());
        createdComment.setUserId(taskActionRequest.getAuthor());

        taskService.saveComment(createdComment);
        taskService.complete(task.getId(), variablesToSet, null);
    }

    public String getParentProcessInstanceId(Task task) {
        String processInstanceId = task.getProcessInstanceId();
        String parentProcessInstanceId = (String) task.getProcessVariables().get(Constants.PROCESS_INSTANCE_ID);
        if (!StringUtils.isEmpty(parentProcessInstanceId)) {
            processInstanceId = parentProcessInstanceId;
        }
        return processInstanceId;
    }
}
