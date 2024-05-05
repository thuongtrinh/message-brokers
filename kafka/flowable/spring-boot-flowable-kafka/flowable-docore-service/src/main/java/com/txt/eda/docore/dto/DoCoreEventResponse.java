package com.txt.eda.docore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoCoreEventResponse implements Serializable {

    private static final long serialVersionUID = -6720574041696675006L;
    private String eventKey;
    private String processInstanceId;
    private Boolean doCoreResult;

    @JsonProperty("is_skip_aq")
    private Boolean isSkipAq;

    @JsonProperty("aq_assignee")
    private String aqAssignee;

    @JsonProperty("candidate_group_aq")
    private String candidateGroupAq;
}
