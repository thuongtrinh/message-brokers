package com.txt.eda.rest.service;


import com.txt.eda.rest.dto.task.TaskActionRequestDTO;
import com.txt.eda.rest.dto.task.TaskDTO;
import org.flowable.rest.service.api.runtime.task.TaskActionRequest;

public interface TaskResourceService {

    TaskDTO getTask(String taskId);

    void executeTaskAction(String taskId, TaskActionRequestDTO taskActionRequest);
}
