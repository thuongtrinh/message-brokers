package com.txt.eda.rest.dto.processInstance;

import lombok.Data;

@Data
public class GenericProcessInstanceDTO {

    protected String id;
    private String exchangeId;
    private String correlationId;
    protected String processInstanceId;
    protected String resource;
    protected String category;
    protected String transType;
    protected Object payload;
    protected String createdBy;
    private String createdDate;

}
