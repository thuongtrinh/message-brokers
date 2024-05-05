package com.txt.eda.rest.dto.task;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.txt.eda.rest.dto.processInstance.GenericProcessInstanceDTO;
import lombok.Data;
import org.flowable.common.rest.util.DateToStringSerializer;

import java.util.Date;


@Data
public class TaskDTO extends GenericProcessInstanceDTO {

    protected String id;
    protected String name;
    protected String owner;
    protected String assignee;
    protected String assigneeName;
    protected String delegationState;//pending || resolved reference to class DelegationState
    protected String description;
    protected boolean suspended;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    protected Date createTime;
    private String activity;
//    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
//    private Date createdDate;

    private String newType;
    private String newSubType;
    private String request;
}
