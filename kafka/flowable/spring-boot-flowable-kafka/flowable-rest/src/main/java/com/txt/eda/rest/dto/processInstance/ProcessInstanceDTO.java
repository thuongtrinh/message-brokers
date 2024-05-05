package com.txt.eda.rest.dto.processInstance;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.common.rest.util.DateToStringSerializer;

import java.util.Date;

@Data
public class ProcessInstanceDTO extends GenericProcessInstanceDTO {

    protected boolean suspended;
    protected boolean ended;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    @Schema(example = "2022-04-17T10:17:43.902+0000")
    protected Date startTime;
    protected boolean completed;
    private String newType;
    private String newSubType;
    private String request;
    private String assignee;
    protected String assigneeName;
}
