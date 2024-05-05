package com.txt.eda.rest.controller;

import com.txt.eda.rest.dto.payload.PayLoadSaveInfoResponse;
import com.txt.eda.rest.dto.payload.PayloadSaveRequest;
import com.txt.eda.rest.service.PayloadInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Payload Info")
@RequestMapping("/api/v1/payload/")
public class PayloadResource {

    private final PayloadInfoService payloadInfoService;

    @Operation(summary = "Get payload info process instances", tags = {"Payload Info"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates the groups were found and returned."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @GetMapping(value = "get-payload-info", produces = "application/json")
    public PayLoadSaveInfoResponse getPayloadInfo(@RequestParam String processInstanceId) {
        return payloadInfoService.getPayLoadSaveInfo(processInstanceId);
    }

    @Operation(summary = "Save payload info process instances", tags = {"Payload Info"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates the groups were found and returned."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")})
    @PostMapping(value = "save-payload-info/{taskId}", produces = "application/json")
    public PayLoadSaveInfoResponse getPayloadSubmission(
            @Parameter(name = "taskId") @PathVariable(name = "taskId") String taskId,
            @RequestBody PayloadSaveRequest request) {
        return payloadInfoService.savePayload(request, taskId);
    }

}
