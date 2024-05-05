package com.txt.eda.rest.dto.processInstance;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.flowable.common.rest.util.DateToStringSerializer;

import java.util.Date;

@Data
public class HistoricProcessInstanceDTO extends GenericProcessInstanceDTO {

    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date startTime;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date endTime;

    protected boolean suspended;
    protected boolean completed;
    private String status;
    private String activity;
    private String request;

}
