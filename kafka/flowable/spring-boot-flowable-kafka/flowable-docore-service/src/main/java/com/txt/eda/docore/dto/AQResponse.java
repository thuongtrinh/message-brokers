package com.txt.eda.docore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AQResponse implements Serializable {

    private static final long serialVersionUID = 5069108805026881385L;

    private String eventKey;

    private String processInstanceId;

    @JsonProperty("aq_assignee")
    private String aqAssignee;

    @JsonProperty("candidate_group_aq")
    private String candidateGroupAq;

    @JsonProperty("is_skip_aq")
    private Boolean isSkipAq;

}
