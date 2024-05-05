package com.txt.eda.rest.controller;

import com.txt.eda.rest.dto.processInstance.HistoricProcessInstanceDTO;
import com.txt.eda.rest.service.HistoricProcessInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "History Process", description = "Manage History Process Instances")
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoricProcessInstanceResource {

    private final HistoricProcessInstanceService historicProcessInstanceService;

    @Operation(summary = "Get a historic process instance", tags = {"History Process"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates that the historic process instances could be found."),
            @ApiResponse(responseCode = "404", description = "Indicates that the historic process instances could not be found.")})
    @GetMapping(value = "/historic-process-instances/{processInstanceId}", produces = "application/json")
    public HistoricProcessInstanceDTO getHistoricProcessInstance(
            @Parameter(name = "processInstanceId") @PathVariable String processInstanceId) {
        return historicProcessInstanceService.getHistoricProcessInstance(processInstanceId);

    }
}
