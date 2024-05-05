package com.txt.eda.rest.dto.processInstance;

import lombok.Data;
import lombok.ToString;
import org.flowable.common.rest.api.PaginateRequest;

import java.util.Collection;
import java.util.Date;

@Data
@ToString
public class ProcessInstanceRequestDTO extends PaginateRequest {

    private Date createdBefore;
    private Date createdAfter;
    private Boolean suspended;
    private Collection<String> resourceIn;
    private Collection<String> transTypeIn;
    private String correlationId;
    private String businessKey;
    private String category;
}
