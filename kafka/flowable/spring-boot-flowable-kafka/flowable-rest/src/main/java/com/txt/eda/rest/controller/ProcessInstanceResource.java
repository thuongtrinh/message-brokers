package com.txt.eda.rest.controller;

import com.txt.eda.rest.dto.processInstance.ProcessInstanceRequestDTO;
import com.txt.eda.rest.dto.processInstance.ProcessInstanceResponseDTO;
import com.txt.eda.rest.service.ProcessInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Process Instances", description = "Manage Process Instances")
@RequestMapping("/api/v1/query")
@RequiredArgsConstructor
public class ProcessInstanceResource {

    private final ProcessInstanceService processInstanceService;

    @Operation(summary = "Query process instances", tags = {
            "Process Instances"}, description = "The request body can contain all possible filters that can be used in the List process instances URL query. On top of these, it's possible to provide an array of variables to include in the query, with their format described here.\n"
            + "\n" + "The general paging and sorting query-parameters can be used for this URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates request was successful and the process-instances are returned"),
            @ApiResponse(responseCode = "400", description = "Indicates a parameter was passed in the wrong format . The status-message contains additional information.")})
    @PostMapping(value = "/process-instances", produces = "application/json")
    public ProcessInstanceResponseDTO queryProcessInstances(@RequestBody ProcessInstanceRequestDTO queryRequest) {
        return processInstanceService.queryProcessInstance(queryRequest);
    }
}
