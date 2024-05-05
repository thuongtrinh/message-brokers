package com.txt.eda.docore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AQRequestDTO {

    private String processInstanceId;

    @JsonProperty("aq_assignee")
    private String aqAssignee;

    @JsonProperty("candidate_group_aq")
    private String candidateGroupAq;
}
