package com.txt.eda.rest.controller;

import com.txt.eda.rest.dto.task.TaskActionRequestDTO;
import com.txt.eda.rest.dto.task.TaskDTO;
import com.txt.eda.rest.service.TaskResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.rest.service.api.runtime.task.TaskActionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Manage Tasks")
@RequiredArgsConstructor
public class TaskResource {

    private final TaskResourceService taskResourceService;

    @Operation(summary = "Get a task", tags = {"Tasks"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Indicates the task was found and returned."),
            @ApiResponse(responseCode = "404", description = "Indicates the requested task was not found.")})
    @GetMapping(value = "/{taskId}", produces = "application/json")
    public TaskDTO getTask(@Parameter(name = "taskId") @PathVariable String taskId, HttpServletRequest request) throws Exception {
        return taskResourceService.getTask(taskId);
    }

    @Operation(summary = "Tasks actions", tags = { "Tasks" })
    @PostMapping(value = "/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void executeTaskAction(@Parameter(name = "taskId") @PathVariable(name = "taskId") String taskId,
                                  @RequestBody TaskActionRequestDTO taskActionRequest) {
        taskResourceService.executeTaskAction(taskId, taskActionRequest);
    }
}
